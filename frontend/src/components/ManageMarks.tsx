import { useState } from "react";
import Navbar from "./Navbar";
 
const months = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 
                'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro']

const days = ['01','02','03','04','05','06','07','08','09',10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31]

export function ManageMarks(){

    const [month, setMonth] = useState('Fevereiro')

    return(
        <>
            <Navbar/>
            <div className="list-marks">
                <header>
                    Gerenciamento de Pontos
                    <div className="select-container">
                        <select onChange={(e) => setMonth(e.target.value)}>
                            {months.map((month) =>
                                <option key={month} value={month}>{month}</option>
                            )}
                        </select>
                        <select>
                            <option>2025</option>
                        </select>
                    </div>
                </header>
                <ul className="marks">

                    <li className="marks-header">
                        <span className="day-label">Dia</span>
                        <span className="point1-label">Ponto 1</span>
                        <span className="point2-label">Ponto 2</span>
                        <span className="type-label">Tipo</span>
                    </li>

                    {days.map((day) =>
                        <li className="day">Dia {day}</li>
                    )}
                </ul>
            </div>
            
        </>
    )
}