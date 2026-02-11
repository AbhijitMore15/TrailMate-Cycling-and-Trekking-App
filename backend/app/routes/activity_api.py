from fastapi import APIRouter, Depends, HTTPException, Header
from sqlalchemy.orm import Session
from datetime import datetime

from app.database.db import SessionLocal
from app.models.activity import Activity
from app.schemas.activity_schema import ActivityCreate, ActivityOut
from app.utils.effort import calculate_effort
from app.utils.auth_utils import verify_token, get_token_from_header

router = APIRouter()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


def verify_bearer_token(authorization: str = Header(None)) -> int:
    """
    Verify Bearer token from Authorization header
    Extracts and validates JWT token, returns user_id
    """
    if not authorization:
        raise HTTPException(status_code=401, detail="Authorization header missing")
    
    token = get_token_from_header(authorization)
    if not token:
        raise HTTPException(status_code=401, detail="Invalid authorization format. Use 'Bearer <token>'")
    
    user_id = verify_token(token)
    if user_id is None:
        raise HTTPException(status_code=401, detail="Invalid or expired token")
    
    return user_id


@router.post("/activities")
def save_activity(
    activity: ActivityCreate,
    db: Session = Depends(get_db),
    user_id: int = Depends(verify_bearer_token)
):
    """
    Save a completed activity
    Path: POST /api/activities
    
    Authorization: Bearer <jwt_token> (Required)
    
    Args:
        activity: ActivityCreate schema with route_id, distance, duration
        user_id: Extracted from JWT token
    
    Returns:
        Saved activity with calculated effort
    """
    # Verify user_id matches (security check)
    if activity.user_id != user_id:
        raise HTTPException(status_code=403, detail="Cannot create activity for another user")
    
    # Calculate effort
    steps, pedals = calculate_effort(activity.distance)
    effort = steps + pedals

    db_activity = Activity(
        user_id=activity.user_id,
        route_id=activity.route_id,
        distance=activity.distance,
        duration=activity.duration,
        effort=effort
    )

    db.add(db_activity)
    db.commit()
    db.refresh(db_activity)

    # Return in format expected by frontend
    return {
        "id": db_activity.id,
        "routeId": db_activity.route_id,
        "userId": db_activity.user_id,
        "distance": db_activity.distance,
        "time": db_activity.duration,
        "steps": steps,
        "pedals": pedals,
        "timestamp": db_activity.created_at.isoformat() if hasattr(db_activity, 'created_at') else datetime.utcnow().isoformat()
    }


@router.get("/activities/user/{user_id}")
def get_activity_history(
    user_id: int,
    db: Session = Depends(get_db),
    authenticated_user_id: int = Depends(verify_bearer_token)
):
    """
    Get activity history for a user
    Path: GET /api/activities/user/{user_id}
    
    Authorization: Bearer <jwt_token> (Required)
    
    Args:
        user_id: User ID to get activities for
        authenticated_user_id: Extracted from JWT token
    
    Returns:
        List of activities for the user
    """
    # User can only view their own activities (or admin can view others)
    if user_id != authenticated_user_id:
        raise HTTPException(status_code=403, detail="Cannot view activities for another user")
    
    activities = db.query(Activity).filter(Activity.user_id == user_id).all()
    
    result = []
    for activity in activities:
        # Recalculate effort
        steps, pedals = calculate_effort(activity.distance)
        
        result.append({
            "id": activity.id,
            "routeId": activity.route_id,
            "userId": activity.user_id,
            "distance": activity.distance,
            "time": activity.duration,
            "steps": steps,
            "pedals": pedals,
            "timestamp": activity.created_at.isoformat() if hasattr(activity, 'created_at') else datetime.utcnow().isoformat()
        })
    
    return result