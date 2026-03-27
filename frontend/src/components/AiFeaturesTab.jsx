import { useState } from 'react'

function ResponseBox({ label, response, loading }) {
  return (
    <div className="mt-3">
      <div className="text-sm font-medium text-gray-600 mb-1">{label}</div>
      <div className="bg-gray-50 border border-gray-200 rounded-lg p-3 min-h-[80px]">
        {loading ? (
          <div className="text-gray-400 animate-pulse text-sm">Loading...</div>
        ) : response ? (
          <div className="message-content text-gray-800 text-sm">{response}</div>
        ) : (
          <div className="text-gray-300 text-sm">Response will appear here</div>
        )}
      </div>
    </div>
  )
}

export default function AiFeaturesTab() {
  const [dogsMessage, setDogsMessage] = useState('')
  const [stuffit, setStuffit] = useState(true)
  const [dogsResponse, setDogsResponse] = useState('')
  const [dogsLoading, setDogsLoading] = useState(false)

  const [ragMessage, setRagMessage] = useState('')
  const [ragResponse, setRagResponse] = useState('')
  const [ragLoading, setRagLoading] = useState(false)

  async function handleDogsSubmit(e) {
    e.preventDefault()
    if (!dogsMessage.trim() || dogsLoading) return
    setDogsLoading(true)
    setDogsResponse('')
    try {
      const params = new URLSearchParams({
        message: dogsMessage,
        stuffit: stuffit ? 'true' : 'false',
      })
      const res = await fetch('/ai/dogs?' + params.toString())
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const text = await res.text()
      setDogsResponse(text)
    } catch (err) {
      setDogsResponse(`Error: ${err.message}`)
    } finally {
      setDogsLoading(false)
    }
  }

  async function handleRagSubmit(e) {
    e.preventDefault()
    if (!ragMessage.trim() || ragLoading) return
    setRagLoading(true)
    setRagResponse('')
    try {
      const res = await fetch(
        '/recommendations?message=' + encodeURIComponent(ragMessage)
      )
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const text = await res.text()
      setRagResponse(text)
    } catch (err) {
      setRagResponse(`Error: ${err.message}`)
    } finally {
      setRagLoading(false)
    }
  }

  return (
    <div className="space-y-8">
      {/* Prompt Stuffing */}
      <div className="bg-white rounded-lg shadow p-5">
        <h2 className="text-lg font-semibold text-gray-800 mb-1">
          Prompt Stuffing
        </h2>
        <p className="text-sm text-gray-500 mb-4">
          Asks the AI about dogs with or without local dog names stuffed into the
          prompt (<code className="bg-gray-100 px-1 rounded">GET /ai/dogs</code>).
        </p>
        <form onSubmit={handleDogsSubmit} className="space-y-3">
          <input
            type="text"
            value={dogsMessage}
            onChange={(e) => setDogsMessage(e.target.value)}
            placeholder="e.g. What are some good dog names?"
            disabled={dogsLoading}
            className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
          />
          <div className="flex items-center gap-4">
            <label className="flex items-center gap-2 cursor-pointer select-none">
              <input
                type="checkbox"
                checked={stuffit}
                onChange={(e) => setStuffit(e.target.checked)}
                className="w-4 h-4"
              />
              <span className="text-sm text-gray-700">
                Stuff with dog names (<code className="bg-gray-100 px-1 rounded">stuffit=true</code>)
              </span>
            </label>
            <button
              type="submit"
              disabled={dogsLoading || !dogsMessage.trim()}
              className="ml-auto bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
            >
              Ask
            </button>
          </div>
        </form>
        <ResponseBox label="Response" response={dogsResponse} loading={dogsLoading} />
      </div>

      {/* RAG Recommendations */}
      <div className="bg-white rounded-lg shadow p-5">
        <h2 className="text-lg font-semibold text-gray-800 mb-1">
          RAG Recommendations
        </h2>
        <p className="text-sm text-gray-500 mb-4">
          Uses the vector store built from{' '}
          <code className="bg-gray-100 px-1 rounded">doggiedetails.txt</code> to
          answer dog breed questions (
          <code className="bg-gray-100 px-1 rounded">GET /recommendations</code>).
        </p>
        <form onSubmit={handleRagSubmit} className="flex gap-2">
          <input
            type="text"
            value={ragMessage}
            onChange={(e) => setRagMessage(e.target.value)}
            placeholder="e.g. What breed is good for apartments?"
            disabled={ragLoading}
            className="flex-1 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
          />
          <button
            type="submit"
            disabled={ragLoading || !ragMessage.trim()}
            className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          >
            Search
          </button>
        </form>
        <ResponseBox label="Recommendation" response={ragResponse} loading={ragLoading} />
      </div>
    </div>
  )
}
