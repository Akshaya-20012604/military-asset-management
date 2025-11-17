import React, { useState } from 'react'
import axios from 'axios'

export default function Login() {
  const [username,setUsername] = useState('admin');
  const [password,setPassword] = useState('demo123');
  const [err,setErr] = useState(null);

  const submit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post('/api/auth/login', { username, password });
      localStorage.setItem('token', res.data.token);
      window.location.href = '/';
    } catch (e) {
      setErr('Login failed');
    }
  }

  return (
    <div className="container">
      <div className="max-w-md mx-auto bg-white p-6 rounded shadow mt-24">
        <h2 className="text-2xl font-semibold mb-4">Sign in</h2>
        {err && <div className="text-red-600 mb-2">{err}</div>}
        <form onSubmit={submit} className="space-y-3">
          <input className="w-full p-2 border rounded" value={username} onChange={e=>setUsername(e.target.value)} />
          <input type="password" className="w-full p-2 border rounded" value={password} onChange={e=>setPassword(e.target.value)} />
          <button className="w-full py-2 bg-blue-600 text-white rounded">Sign in</button>
        </form>
      </div>
    </div>
  )
}
