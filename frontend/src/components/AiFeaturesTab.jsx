import { useState } from 'react'

export default function AiFeaturesTab({ addMessage, ragFile, setRagFile, useRagFile, setUseRagFile }) {
  const [dogsMessage, setDogsMessage] = useState('')
  const [stuffit, setStuffit] = useState(true)
  const [dogsLoading, setDogsLoading] = useState(false)

  async function handleDogsSubmit(e) {
    e.preventDefault()
    if (!dogsMessage.trim() || dogsLoading) return
    const text = dogsMessage.trim()
    setDogsMessage('')
    setDogsLoading(true)
    addMessage({ role: 'user', content: text })
    try {
      const params = new URLSearchParams({
        message: text,
        stuffit: stuffit ? 'true' : 'false',
      })
      const res = await fetch('/ai/dogs?' + params.toString())
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const reply = await res.text()
      addMessage({ role: 'bot', content: reply })
    } catch (err) {
      addMessage({ role: 'system', content: `Error: ${err.message}` })
    } finally {
      setDogsLoading(false)
    }
  }

  return (
    <div className="space-y-8">
      {/* Prompt Stuffing */}
      <div className="bg-white rounded-lg shadow p-5">
        <h2 className="text-lg font-semibold text-gray-800 mb-3">
          Prompt Stuffing
        </h2>
        <label className="flex items-center gap-2 cursor-pointer select-none mb-3">
          <input
            type="checkbox"
            checked={stuffit}
            onChange={(e) => setStuffit(e.target.checked)}
            className="w-4 h-4"
          />
          <span className="text-sm text-gray-700">
            Stuff the prompt with some custom text
          </span>
        </label>
        <form onSubmit={handleDogsSubmit}>
          <input
            type="text"
            value={dogsMessage}
            onChange={(e) => setDogsMessage(e.target.value)}
            disabled={dogsLoading}
            className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
          />
        </form>
      </div>

      {/* RAG Recommendations */}
      <div className="bg-white rounded-lg shadow p-5">
        <h2 className="text-lg font-semibold text-gray-800 mb-3">
          RAG Recommendations
        </h2>
        <label className="flex items-center gap-2 cursor-pointer select-none mb-3">
          <input
            type="checkbox"
            checked={useRagFile}
            onChange={(e) => setUseRagFile(e.target.checked)}
            disabled={!ragFile}
            className="w-4 h-4"
          />
          <span className={`text-sm ${ragFile ? 'text-gray-700' : 'text-gray-400'}`}>
            Use uploaded file for RAG
          </span>
        </label>
        <input
          type="file"
          accept=".txt,.md"
          onChange={(e) => { setRagFile(e.target.files[0] || null); setUseRagFile(false) }}
          className="block w-full text-sm text-gray-500 file:mr-3 file:py-1.5 file:px-3 file:rounded-lg file:border-0 file:text-sm file:font-medium file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
        />
      </div>
    </div>
  )
}
