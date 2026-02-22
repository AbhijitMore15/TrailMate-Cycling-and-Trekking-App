from fastapi import APIRouter, Query
from pydantic import BaseModel
from typing import Optional
import json
import os

router = APIRouter()

# =====================
# FILE PATH
# =====================
HISTORY_FILE = "app/database/calories_history.json"


# =====================
# JSON HELPERS
# =====================
def load_history():
    if not os.path.exists(HISTORY_FILE):
        return []

    with open(HISTORY_FILE, "r") as f:
        return json.load(f)


def save_history(history):
    with open(HISTORY_FILE, "w") as f:
        json.dump(history, f, indent=4)


# =====================
# MODELS
# =====================
class CaloriesRequest(BaseModel):
    activity: str
    weight: float
    duration_hours: Optional[float] = None
    distance_km: Optional[float] = None


class CaloriesResponse(BaseModel):
    calories_burned: float


# =====================
# CONSTANTS
# =====================
MET_VALUES = {
    "trekking": 6.5,
    "cycling": 7.5
}

AVG_SPEED = {
    "trekking": 4,
    "cycling": 15
}


# =====================
# CALCULATE API
# =====================
@router.post("/calculate", response_model=CaloriesResponse)
def calculate_calories(
    request: CaloriesRequest,
    user_id: int = Query(..., description="Logged in user id")
):

    history = load_history()

    activity = request.activity.lower()
    met = MET_VALUES.get(activity, 6.0)

    duration = request.duration_hours or 1
    distance = request.distance_km or 0

    base_calories = met * request.weight * duration

    reference_distance = {
        "trekking": 5,
        "cycling": 15
    }.get(activity, 10)

    distance_factor = 1 + (distance / reference_distance)

    final_calories = base_calories * distance_factor

    # ⭐ RECORD WITH USER ID
    record = {
        "user_id": user_id,
        "activity": activity,
        "weight": request.weight,
        "duration": duration,
        "distance": distance,
        "calories": round(final_calories, 2)
    }

    history.append(record)
    save_history(history)

    return CaloriesResponse(
        calories_burned=record["calories"]
    )


# =====================
# HISTORY API
# =====================
@router.get("/history/{user_id}")
def get_calories_history(user_id: int):

    history = load_history()

    user_history = [h for h in history if h.get("user_id") == user_id]

    return user_history
