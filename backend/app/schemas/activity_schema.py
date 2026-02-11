from pydantic import BaseModel

class ActivityCreate(BaseModel):
    user_id: int
    route_id: int
    distance: float
    duration: float

class ActivityOut(BaseModel):
    id: int
    route_id: int
    distance: float
    duration: float
    effort: float

    class Config:
        from_attributes = True
