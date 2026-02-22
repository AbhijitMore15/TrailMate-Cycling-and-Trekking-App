from fastapi import APIRouter
import json
import os

router = APIRouter()

FILE_PATH = "app/database/calories_history.json"


@router.get("/weekly-distance/{user_id}")
def weekly_distance(user_id: int):

    if not os.path.exists(FILE_PATH):
        return []

    with open(FILE_PATH, "r") as f:
        data = json.load(f)

    # filter user data
    user_data = [d for d in data if d.get("user_id") == user_id]

    # last 7 entries
    user_data = user_data[-7:]

    return [
        {
            "label": f"Day {i+1}",
            "distance": item.get("distance", 0)
        }
        for i, item in enumerate(user_data)
    ]
