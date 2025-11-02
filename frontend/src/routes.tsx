import {BrowserRouter, Route, Routes, Navigate} from 'react-router-dom'
import Home from './components/Home'

export const AppRoutes = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path='/' element={<Home/>}/>
                <Route path='/view-marks' element={<h1>teste</h1>}/>
                <Route path='/manage-marks' element={<h1>teste 2</h1>}/>
            </Routes>
        </BrowserRouter>
    )
}