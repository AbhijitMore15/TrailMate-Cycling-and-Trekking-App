from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.database.db import SessionLocal
from app.models.route import Route

router = APIRouter()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.get("/routes")
def get_routes(db: Session = Depends(get_db)):
    """
    Get all routes
    Path: GET /api/routes
    
    Returns:
        List of all routes with complete information
    """
    routes = db.query(Route).all()
    
    # Convert to proper format expected by frontend
    result = []
    for route in routes:
        route_dict = {
            "id": route.id,
            "name": route.name,
            "distance": route.distance,
            "elevation": route.elevation,
            "duration": route.duration,
            "latitude": route.latitude,
            "longitude": route.longitude,
            "coordinates": route.coordinates if route.coordinates else []
        }
        result.append(route_dict)
    
    return result


@router.get("/routes/{route_id}")
def get_route_details(route_id: int, db: Session = Depends(get_db)):
    """
    Get details of a specific route
    Path: GET /api/routes/{route_id}
    
    Args:
        route_id: ID of the route
    
    Returns:
        Route object with all details
    """
    route = db.query(Route).filter(Route.id == route_id).first()
    
    if not route:
        raise HTTPException(status_code=404, detail="Route not found")
    
    return {
        "id": route.id,
        "name": route.name,
        "distance": route.distance,
        "elevation": route.elevation,
        "duration": route.duration,
        "latitude": route.latitude,
        "longitude": route.longitude,
        "coordinates": route.coordinates if route.coordinates else []
    }