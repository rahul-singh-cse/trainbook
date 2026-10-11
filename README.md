# TrainBook 🚆
Full stack train ticket booking app.
## Live Demo :## 🔗 Live Demo
**Frontend:** [trainbook-five.vercel.app](https://trainbook-five.vercel.app)

> Backend free server pe hai, pehli baar load hone me 40-60 second lag sakte hain.
**Stack:** Java 17, Spring Boot 3, Spring Data JPA, H2 (file DB) + React 18 (Vite)

## Features
- Search trains by source/destination
- Book tickets with live seat availability and fare calculation
- Unique PNR per booking
- Cancel booking (seats restored)
- Seed data loaded on first run

## Run
Backend (port 8080):
    cd backend && mvn spring-boot:run
Frontend (port 5173):
    cd frontend && npm install && npm run dev

## API
- GET /api/trains?from=&to=
- POST /api/bookings  {trainId, passengerName, seats}
- GET /api/bookings
- DELETE /api/bookings/{id}

## Next steps
JWT login, seat selection, payment mock, PostgreSQL, Docker.
