from sqlalchemy import Column, Integer, Float, ForeignKey
from app.database.db import Base

class Activity(Base):
    __tablename__ = "activities"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"))
    route_id = Column(Integer, ForeignKey("routes.id"))
    distance = Column(Float)
    duration = Column(Float)
    effort = Column(Float)
