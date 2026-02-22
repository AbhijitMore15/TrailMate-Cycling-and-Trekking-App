from sqlalchemy import Column, Integer, String, Float
from ..database.db import Base

class Route(Base):
    __tablename__ = "routes"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String)
    distance = Column(Float)
    elevation = Column(Float)
    coordinates = Column(String)
