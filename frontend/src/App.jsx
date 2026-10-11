import { useEffect, useState } from 'react'
const API = 'http://localhost:8080/api'

export default function App() {
  const [from, setFrom] = useState('')
  const [to, setTo] = useState('')
  const [trains, setTrains] = useState([])
  const [bookings, setBookings] = useState([])
  const [selected, setSelected] = useState(null)
  const [name, setName] = useState('')
  const [seats, setSeats] = useState(1)
  const [msg, setMsg] = useState('')

  const loadTrains = () =>
    fetch(`${API}/trains?from=${from}&to=${to}`).then(r => r.json()).then(setTrains).catch(() => setMsg('Backend not running on :8080'))
  const loadBookings = () => fetch(`${API}/bookings`).then(r => r.json()).then(setBookings).catch(() => {})

  useEffect(() => { loadTrains(); loadBookings() }, [])

  const book = async () => {
    const res = await fetch(`${API}/bookings`, {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ trainId: selected.id, passengerName: name, seats: Number(seats) })
    })
    if (res.ok) {
      const b = await res.json()
      setMsg(`Booked! PNR ${b.pnr}, total ₹${b.totalFare}`)
      setSelected(null); setName(''); setSeats(1); loadTrains(); loadBookings()
    } else {
      const e = await res.json(); setMsg(e.message || 'Booking failed')
    }
  }

  const cancel = async id => {
    await fetch(`${API}/bookings/${id}`, { method: 'DELETE' })
    setMsg('Booking cancelled'); loadTrains(); loadBookings()
  }

  return (
    <div className="wrap">
      <header><h1>🚆 TrainBook</h1><p>Search trains and book tickets</p></header>
      {msg && <div className="msg" onClick={() => setMsg('')}>{msg}</div>}

      <div className="search">
        <input placeholder="From (e.g. Jaipur)" value={from} onChange={e => setFrom(e.target.value)} />
        <input placeholder="To (e.g. Delhi)" value={to} onChange={e => setTo(e.target.value)} />
        <button onClick={loadTrains}>Search</button>
      </div>

      <h2>Available Trains</h2>
      {trains.length === 0 && <p className="muted">No trains found.</p>}
      {trains.map(t => (
        <div className="card" key={t.id}>
          <div>
            <b>{t.name}</b>
            <div className="muted">{t.fromStation} → {t.toStation} · {t.departure} - {t.arrival}</div>
          </div>
          <div className="right">
            <div>₹{t.fare} · {t.availableSeats} seats</div>
            <button disabled={t.availableSeats === 0} onClick={() => setSelected(t)}>
              {t.availableSeats === 0 ? 'Sold out' : 'Book'}
            </button>
          </div>
        </div>
      ))}

      {selected && (
        <div className="modal"><div className="box">
          <h3>Book {selected.name}</h3>
          <input placeholder="Passenger name" value={name} onChange={e => setName(e.target.value)} />
          <input type="number" min="1" max={selected.availableSeats} value={seats} onChange={e => setSeats(e.target.value)} />
          <p>Total: ₹{selected.fare * seats}</p>
          <button onClick={book}>Confirm</button>
          <button className="ghost" onClick={() => setSelected(null)}>Cancel</button>
        </div></div>
      )}

      <h2>My Bookings</h2>
      {bookings.length === 0 && <p className="muted">No bookings yet.</p>}
      {bookings.map(b => (
        <div className="card" key={b.id}>
          <div>
            <b>{b.trainName}</b> <span className="pnr">PNR {b.pnr}</span>
            <div className="muted">{b.route} · {b.passengerName} · {b.seats} seat(s)</div>
          </div>
          <div className="right">
            <div>₹{b.totalFare}</div>
            <button className="ghost" onClick={() => cancel(b.id)}>Cancel</button>
          </div>
        </div>
      ))}
    </div>
  )
}
