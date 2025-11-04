import {BrowserRouter, Route, Routes, Navigate} from 'react-router-dom'
import Home from './components/Home'
import { ManageMarks } from './components/ManageMarks'

export const AppRoutes = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path='/' element={<Home/>}/>
                <Route path='/manage-marks' element={<ManageMarks/>}/>
            </Routes>
        </BrowserRouter>
    )
}