import json

difficulty_rank = {
    "beginner": 1,
    "intermediate": 2,
    "advanced": 3
}

def load_equipment():
    with open("app/data/equipment_master.json") as f:
        return json.load(f)


def recommend_equipment(request):

    equipment_list = load_equipment()
    user_level = difficulty_rank[request.difficulty]

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
