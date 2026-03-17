import { useState, useEffect } from 'react'

export default function DogsTab() {
  const [dogs, setDogs] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [isSearchResult, setIsSearchResult] = useState(false)

  const [name, setName] = useState('')
  const [breed, setBreed] = useState('')
  const [description, setDescription] = useState('')

  useEffect(() => {
    loadAllDogs()
  }, [])

  async function loadAllDogs() {
    setLoading(true)
    setError('')
    try {
      const res = await fetch('/dogs')
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const data = await res.json()
      setDogs(data)
      setIsSearchResult(false)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  async function handleSearch(e) {
    e.preventDefault()
    setLoading(true)
    setError('')
    try {
      const params = new URLSearchParams()
      if (name.trim()) params.set('name', name.trim())
      if (breed.trim()) params.set('breed', breed.trim())
      if (description.trim()) params.set('description', description.trim())
      const res = await fetch('/dogs/search?' + params.toString())
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const data = await res.json()
      setDogs(data)
      setIsSearchResult(true)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  function handleClear() {
    setName('')
    setBreed('')
    setDescription('')
    loadAllDogs()
  }

  return (
    <div className="space-y-4">
      {/* Search form */}
      <div className="bg-white rounded-lg shadow p-4">
        <h2 className="text-lg font-semibold text-gray-800 mb-3">Search Dogs</h2>
        <form onSubmit={handleSearch} className="flex flex-wrap gap-3 items-end">
          <div className="flex flex-col gap-1">
            <label className="text-xs text-gray-500 font-medium">Name</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="e.g. Buddy"
              className="p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 w-40"
            />
          </div>
          <div className="flex flex-col gap-1">
            <label className="text-xs text-gray-500 font-medium">Breed</label>
            <input
              type="text"
              value={breed}
              onChange={(e) => setBreed(e.target.value)}
              placeholder="e.g. Labrador"
              className="p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 w-40"
            />
          </div>
          <div className="flex flex-col gap-1">
            <label className="text-xs text-gray-500 font-medium">Description</label>
            <input
              type="text"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="e.g. friendly"
              className="p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 w-48"
            />
          </div>
          <div className="flex gap-2">
            <button
              type="submit"
              disabled={loading}
              className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 disabled:opacity-50 transition-colors"
            >
              Search
            </button>
            {isSearchResult && (
              <button
                type="button"
                onClick={handleClear}
                className="bg-gray-200 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-300 transition-colors"
              >
                Clear
              </button>
            )}
          </div>
        </form>
      </div>

      {/* Results */}
      <div className="bg-white rounded-lg shadow">
        <div className="px-4 py-3 border-b border-gray-200 flex items-center justify-between">
          <h2 className="text-lg font-semibold text-gray-800">
            {isSearchResult ? 'Search Results' : 'All Dogs'}
          </h2>
          <span className="text-sm text-gray-500">
            {loading ? 'Loading...' : `${dogs.length} dog${dogs.length !== 1 ? 's' : ''}`}
          </span>
        </div>

        {error && (
          <div className="p-4 text-red-600 text-sm">Error: {error}</div>
        )}

        {!loading && !error && dogs.length === 0 && (
          <div className="p-8 text-center text-gray-400">No dogs found.</div>
        )}

        {dogs.length > 0 && (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead className="bg-gray-50 text-gray-600 text-left">
                <tr>
                  <th className="px-4 py-2 font-medium">ID</th>
                  <th className="px-4 py-2 font-medium">Name</th>
                  <th className="px-4 py-2 font-medium">Breed</th>
                  <th className="px-4 py-2 font-medium">Description</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {dogs.map((dog) => (
                  <tr key={dog.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-4 py-2 text-gray-400">{dog.id}</td>
                    <td className="px-4 py-2 font-medium text-gray-800">{dog.name}</td>
                    <td className="px-4 py-2 text-gray-600">{dog.breed}</td>
                    <td className="px-4 py-2 text-gray-500">{dog.description}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  )
}
