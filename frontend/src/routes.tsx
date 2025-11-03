import {BrowserRouter, Route, Routes, Navigate} from 'react-router-dom'
import Home from './components/Home'
import { ManageMarks } from './components/ManageMarks'
import { ViewMarks } from './components/ViewMarks'

export const AppRoutes = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path='/' element={<Home/>}/>
                <Route path='/view-marks' element={<ViewMarks/>}/>
                <Route path='/manage-marks' element={<ManageMarks/>}/>
            </Routes>
        </BrowserRouter>
    )
}