from fastapi import APIRouter
from pydantic import BaseModel
import json

router = APIRouter()


class EquipmentRequest(BaseModel):
    activity: str
    difficulty: str
    duration_hours: int


# Load JSON Data
def load_equipment():
    with open("app/database/equipment_master.json") as f:
        return json.load(f)


@router.post("/recommend")
def recommend_equipment(request: EquipmentRequest):

    equipment_list = load_equipment()

    difficulty_rank = {
        "beginner": 1,
        "intermediate": 2,
        "advanced": 3
    }

    user_level = difficulty_rank.get(request.difficulty, 1)

    results = []

    for item in equipment_list:

        if request.activity not in item["activity"]:
            continue

        allowed = any(
            difficulty_rank[level] <= user_level
            for level in item["difficulty"]
        )

        if not allowed:
            continue

        if request.duration_hours < item["duration_min_hours"]:
            continue

        results.append(item)

    return results
