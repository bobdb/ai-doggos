import { useState, useEffect } from 'react'
import ChatTab from './components/ChatTab.jsx'
import AiFeaturesTab from './components/AiFeaturesTab.jsx'
import DogsTab from './components/DogsTab.jsx'

const statusConfig = {
  checking: { dot: 'bg-gray-400', text: 'text-gray-500', label: 'Checking backend…' },
  up:       { dot: 'bg-green-500', text: 'text-green-600', label: 'Backend online' },
  down:     { dot: 'bg-red-500',   text: 'text-red-600',   label: 'Backend offline' },
}

function App() {
  const [activeView, setActiveView] = useState('dogchat')
  const [backendStatus, setBackendStatus] = useState('checking')
  const [messages, setMessages] = useState([])
  const addMessage = (msg) => setMessages(prev => [...prev, msg])
  const [ragFile, setRagFile] = useState(null)
  const [useRagFile, setUseRagFile] = useState(false)

  useEffect(() => {
    fetch('/actuator/health')
      .then(res => setBackendStatus(res.ok ? 'up' : 'down'))
      .catch(() => setBackendStatus('down'))
  }, [])

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      <header className="bg-white shadow">
        <div className="max-w-5xl mx-auto px-4 py-4">
          <h1 className="text-2xl font-bold text-gray-800 text-center mb-4">
            🐶 AI Doggos
          </h1>
          <div className="flex justify-center mb-3">
            {(() => {
              const { dot, text, label } = statusConfig[backendStatus]
              return (
                <span className={`flex items-center gap-1.5 text-sm ${text}`}>
                  <span className={`inline-block w-2 h-2 rounded-full ${dot}`} />
                  {label}
                </span>
              )
            })()}
          </div>
          <nav className="flex gap-2 justify-center">
            <button
              onClick={() => setActiveView('dogchat')}
              className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                activeView === 'dogchat'
                  ? 'bg-blue-500 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              DogChat
            </button>
            <button
              onClick={() => setActiveView('dogcatalog')}
              className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                activeView === 'dogcatalog'
                  ? 'bg-blue-500 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              DogCatalog
            </button>
          </nav>
        </div>
      </header>

      <main className="flex-1 max-w-5xl w-full mx-auto px-4 py-6">
        {activeView === 'dogchat' && (
          <div className="flex gap-4 h-[calc(100vh-180px)]">
            <div className="flex-1 min-w-0">
              <ChatTab messages={messages} setMessages={setMessages} ragFile={ragFile} useRagFile={useRagFile} />
            </div>
            <div className="w-96 overflow-y-auto">
              <AiFeaturesTab addMessage={addMessage} ragFile={ragFile} setRagFile={setRagFile} useRagFile={useRagFile} setUseRagFile={setUseRagFile} />
            </div>
          </div>
        )}
        {activeView === 'dogcatalog' && <DogsTab />}
      </main>
    </div>
  )
}

export default App
