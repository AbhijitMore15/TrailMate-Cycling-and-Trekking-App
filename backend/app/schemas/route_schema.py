from pydantic import BaseModel

class RouteOut(BaseModel):
    id: int
    name: str
    distance: float
    elevation: float
    coordinates: str
