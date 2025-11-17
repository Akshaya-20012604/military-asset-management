import React, { useEffect, useState } from 'react'
import axios from 'axios'

axios.defaults.baseURL = '/'

axios.interceptors.request.use(cfg => {
  const token = localStorage.getItem('token')
  if (token) cfg.headers.Authorization = `Bearer ${token}`
  return cfg
})

export default function Dashboard() {
  const [summary, setSummary] = useState(null)
  useEffect(()=>{ load() }, [])
  async function load() {
    try {
      const res = await axios.get('/api/dashboard/summary')
      setSummary(res.data)
    } catch (e) {
      console.error(e)
    }
  }
  return (
    <div className="container">
      <div className="bg-white p-4 rounded shadow mt-6">
        <h1 className="text-2xl font-semibold">Asset Dashboard</h1>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-4">
          <div className="p-4 border rounded">
            <div className="text-sm">Opening Balance</div>
            <div className="text-xl">{summary?.openingBalance ?? '—'}</div>
          </div>
          <div className="p-4 border rounded">
            <div className="text-sm">Net Movement</div>
            <div className="text-xl">{summary?.netMovement ?? '—'}</div>
          </div>
          <div className="p-4 border rounded">
            <div className="text-sm">Closing Balance</div>
            <div className="text-xl">{summary?.closingBalance ?? '—'}</div>
          </div>
        </div>
        <div className="mt-4">
          <h3 className="font-medium">Breakdown</h3>
          <pre className="bg-gray-50 p-2 rounded">{JSON.stringify(summary?.breakdown, null, 2)}</pre>
        </div>
      </div>
    </div>
  )
}
