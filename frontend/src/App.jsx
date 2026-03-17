import { useState } from 'react'
import ChatTab from './components/ChatTab.jsx'
import AiFeaturesTab from './components/AiFeaturesTab.jsx'
import DogsTab from './components/DogsTab.jsx'

function App() {
  const [activeView, setActiveView] = useState('dogchat')

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      <header className="bg-white shadow">
        <div className="max-w-5xl mx-auto px-4 py-4">
          <h1 className="text-2xl font-bold text-gray-800 text-center mb-4">
            🐶 AI Doggos
          </h1>
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
          <>
            <ChatTab />
            <AiFeaturesTab />
          </>
        )}
        {activeView === 'dogcatalog' && <DogsTab />}
      </main>
    </div>
  )
}

export default App
