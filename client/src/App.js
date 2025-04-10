import MainPage from './page/MainPage.js'
import {Route ,Routes} from 'react-router-dom'


function App() {
  return (
    <Routes>
      <Route path="/" element={<MainPage />}/>
    </Routes>
  )
}

export default App