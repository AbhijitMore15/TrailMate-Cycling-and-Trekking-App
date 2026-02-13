from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from ..database.db import SessionLocal
from app.models.user import User
from ..schemas.user_schema import UserCreate, UserLogin
from ..utils.auth_utils import (
    hash_password,
    verify_password,
    create_access_token
)

router = APIRouter(tags=["Auth"])


def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


@router.post("/register")
def register(user: UserCreate, db: Session = Depends(get_db)):
    # Check if user already exists
    existing_user = (
        db.query(User)
        .filter(User.email == user.email)
        .first()
    )

    if existing_user:
        raise HTTPException(
            status_code=409,  # Conflict
            detail={
                "message": "User already registered",
                "email": user.email,
                "action": "login"
            }
        )

    hashed_password = hash_password(user.password)

    db_user = User(
        name=user.name,
        email=user.email,
        password=hashed_password,
        fitness_level=user.fitness_level
    )

    db.add(db_user)
    db.commit()
    db.refresh(db_user)

    token = create_access_token({"sub": str(db_user.id)})

    return {
        "message": "User registered successfully",
        "token": token,
        "userId": db_user.id,
        "email": db_user.email
    }

@router.post("/login")
def login(user: UserLogin, db: Session = Depends(get_db)):
    db_user = db.query(User).filter(User.email == user.email).first()

    if not db_user or not verify_password(user.password, db_user.password):
        raise HTTPException(status_code=401, detail="Invalid credentials")

    token = create_access_token({"sub": str(db_user.id)})

    return {
        "token": token,
        "userId": db_user.id,
        "email": db_user.email,
        "message": "Login successful"
    }
