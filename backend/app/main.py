from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.database.db import Base, engine
from app.routes import auth, routes_api, activity_api
from app.routes.equipment import router as equipment_router
from app.routes.calories import router as calories_router
from app.routes import stats
from .database.db import Base, engine
from .routes import auth, routes_api, activity_api

Base.metadata.create_all(bind=engine)

app = FastAPI(title="TrailMate API")

# ================= CORS =================

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ================= ROUTERS =================

app.include_router(auth.router, prefix="/api/auth")
app.include_router(routes_api.router, prefix="/api")
app.include_router(activity_api.router, prefix="/api")
app.include_router(equipment_router, prefix="/api/equipment")
app.include_router(calories_router, prefix="/api/calories")
app.include_router(stats.router, prefix="/api/stats", tags=["Stats"])

@app.get("/")
def root():
    return {"message": "TrailMate API running"}

def read_root():
    return {"message": "TrailMate API is running!"}
@app.on_event("startup")
def startup():
    Base.metadata.create_all(bind=engine)
