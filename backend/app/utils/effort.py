def calculate_effort(distance_km: float):
    steps = distance_km * 1300   # average steps per km
    pedals = distance_km * 500   # average pedal strokes per km
    return steps, pedals
