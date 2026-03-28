import { useState, useRef, useEffect } from 'react'

export default function ChatTab({ messages, setMessages }) {
  const [mode, setMode] = useState('blocking')
  const [input, setInput] = useState('')
  const [loading, setLoading] = useState(false)
  const messagesEndRef = useRef(null)

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  async function sendMessage(e) {
    e.preventDefault()
    if (!input.trim() || loading) return

    const text = input.trim()
    setInput('')
    setMessages((prev) => [...prev, { role: 'user', content: text }])
    setLoading(true)

    if (mode === 'blocking') {
      await sendBlocking(text)
    } else {
      await sendStreaming(text)
    }
    setLoading(false)
  }

  async function sendBlocking(text) {
    try {
      const res = await fetch('/chat-blocking?message=' + encodeURIComponent(text), {
        method: 'POST',
      })
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const reply = await res.text()
      setMessages((prev) => [...prev, { role: 'bot', content: reply }])
    } catch (err) {
      setMessages((prev) => [
        ...prev,
        { role: 'system', content: `Error: ${err.message}` },
      ])
    }
  }

  async function sendStreaming(text) {
    const botMsg = { role: 'bot', content: '' }
    setMessages((prev) => [...prev, botMsg])
    const idx = -1 // we'll track via functional update

    try {
      const res = await fetchWithRetry(
        '/chat-streaming?message=' + encodeURIComponent(text)
      )
      const reader = res.body.getReader()
      const decoder = new TextDecoder('utf-8')

      while (true) {
        const { done, value } = await reader.read()
        if (done) break
        const chunk = decoder.decode(value, { stream: true })
        setMessages((prev) => {
          const updated = [...prev]
          // last message is the bot placeholder we just added
          updated[updated.length - 1] = {
            ...updated[updated.length - 1],
            content: updated[updated.length - 1].content + chunk,
          }
          return updated
        })
      }
    } catch (err) {
      setMessages((prev) => {
        const updated = [...prev]
        updated[updated.length - 1] = {
          role: 'system',
          content: `Error: ${err.message}`,
        }
        return updated
      })
    }
  }

  async function fetchWithRetry(url, retries = 3) {
    for (let i = 0; i < retries; i++) {
      try {
        const res = await fetch(url)
        if (!res.ok) throw new Error(`HTTP ${res.status}`)
        return res
      } catch (err) {
        if (i === retries - 1) throw err
        await new Promise((r) => setTimeout(r, 1000))
      }
    }
  }

  return (
    <div className="flex flex-col h-full">
      {/* Mode toggle */}
      <div className="flex gap-2 mb-4">
        <span className="text-sm text-gray-600 self-center">Mode:</span>
        {['blocking', 'streaming'].map((m) => (
          <button
            key={m}
            onClick={() => setMode(m)}
            className={`px-3 py-1 rounded text-sm font-medium transition-colors ${
              mode === m
                ? 'bg-green-500 text-white'
                : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
            }`}
          >
            {m.charAt(0).toUpperCase() + m.slice(1)}
          </button>
        ))}
      </div>

      {/* Messages */}
      <div className="flex-1 overflow-y-auto bg-white rounded-lg shadow p-4 space-y-3 mb-4">
        {messages.length === 0 && (
          <p className="text-gray-400 text-center text-sm mt-8">
            Send a message to start chatting...
          </p>
        )}
        {messages.map((msg, i) => (
          <div
            key={i}
            className={`p-3 rounded-lg ${
              msg.role === 'user'
                ? 'bg-blue-100'
                : msg.role === 'system'
                ? 'bg-red-100'
                : 'bg-gray-100'
            }`}
          >
            <div
              className={`font-bold text-sm mb-1 ${
                msg.role === 'user'
                  ? 'text-blue-600'
                  : msg.role === 'system'
                  ? 'text-red-600'
                  : 'text-green-600'
              }`}
            >
              {msg.role === 'user' ? 'You' : msg.role === 'system' ? 'System' : 'Bot'}
            </div>
            <div className="message-content text-gray-800">{msg.content}</div>
          </div>
        ))}
        {loading && mode === 'blocking' && (
          <div className="bg-gray-100 p-3 rounded-lg">
            <div className="font-bold text-sm text-green-600 mb-1">Bot</div>
            <div className="text-gray-400 animate-pulse">Thinking...</div>
          </div>
        )}
        <div ref={messagesEndRef} />
      </div>

      {/* Input */}
      <form onSubmit={sendMessage} className="flex gap-2">
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Type your message..."
          disabled={loading}
          className="flex-1 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
        />
        <button
          type="submit"
          disabled={loading || !input.trim()}
          className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
        >
          Send
        </button>
      </form>
    </div>
  )
}
