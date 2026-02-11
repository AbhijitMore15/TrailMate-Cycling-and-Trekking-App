from pydantic import BaseModel, EmailStr,Field
from enum import Enum

# -------------------------------
# Fitness Level Enum
# -------------------------------
class FitnessLevel(str, Enum):
    beginner = "beginner"
    intermediate = "intermediate"
    advanced = "advanced"


class UserCreate(BaseModel):
    name: str
    email: EmailStr
    password: str
    fitness_level: FitnessLevel  # ✅ restricted values


class UserLogin(BaseModel):
    email: EmailStr
    password: str


class UserOut(BaseModel):
    id: int
    name: str
    email: str
    fitness_level: FitnessLevel

    class Config:
        from_attributes = True

class RegisterRequest(BaseModel):
    name: str
    email: str
    password: str
    fitness_level: str = Field(..., alias="fitnessLevel")

    class Config:
        populate_by_name = True