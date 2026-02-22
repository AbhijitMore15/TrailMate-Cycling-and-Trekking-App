from pydantic import BaseModel, EmailStr, Field
from enum import Enum


# ================================
# Fitness Level Enum
# ================================
class FitnessLevel(str, Enum):
    beginner = "beginner"
    intermediate = "intermediate"
    advanced = "advanced"


# ================================
# REGISTER REQUEST
# ================================
class UserCreate(BaseModel):
    name: str
    email: EmailStr
    password: str
    fitness_level: FitnessLevel = Field(..., alias="fitnessLevel")

    class Config:
        populate_by_name = True


# ================================
# LOGIN REQUEST
# ================================
class UserLogin(BaseModel):
    email: EmailStr
    password: str


# ================================
# USER RESPONSE MODEL
# ================================
class UserOut(BaseModel):
    id: int
    name: str
    email: EmailStr
    fitness_level: FitnessLevel

    class Config:
        from_attributes = True
