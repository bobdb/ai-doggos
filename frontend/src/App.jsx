import { useState } from 'react'
import ChatTab from './components/ChatTab.jsx'
import AiFeaturesTab from './components/AiFeaturesTab.jsx'
import DogsTab from './components/DogsTab.jsx'

const TABS = ['Chat', 'AI Features', 'Dogs']

function App() {
  const [activeTab, setActiveTab] = useState('Chat')

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      <header className="bg-white shadow">
        <div className="max-w-5xl mx-auto px-4 py-4">
          <h1 className="text-2xl font-bold text-gray-800 text-center mb-4">
            🐶 AI Doggos
          </h1>
          <nav className="flex gap-2 justify-center">
            {TABS.map((tab) => (
              <button
                key={tab}
                onClick={() => setActiveTab(tab)}
                className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                  activeTab === tab
                    ? 'bg-blue-500 text-white'
                    : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                }`}
              >
                {tab}
              </button>
            ))}
          </nav>
        </div>
      </header>

      <main className="flex-1 max-w-5xl w-full mx-auto px-4 py-6">
        {activeTab === 'Chat' && <ChatTab />}
        {activeTab === 'AI Features' && <AiFeaturesTab />}
        {activeTab === 'Dogs' && <DogsTab />}
      </main>
    </div>
  )
}

export default App
