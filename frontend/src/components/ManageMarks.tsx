import {useState } from "react";
import Navbar from "./Navbar";
import Popup from "reactjs-popup";

type MarkType = 'ENTRY' | 'EXIT';


interface Mark {
    id: number,
    user: string;
    markTime: string;
    markDate: string;
    type: MarkType;
}

const months = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 
                'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro']

export function ManageMarks(){

    const [marks, setMarks] = useState<Mark[]>([]);
    const [mark, setMark] = useState<Mark>();

    const handleChange = async(e: React.ChangeEvent<HTMLSelectElement>) => {
        const numMonth = e.target.selectedIndex + 1;
        try {
            const response = await fetch(`http://localhost:8080/api/v1/marks?month=${numMonth}&year=2025`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });

            const json = await response.json()
            const data = json.data
            setMarks(data);
        } catch (err) {
            console.error(err);
        } 
    }
    
    return(
        <>
            <Navbar/>
            <div className="list-marks">
                <header>
                    Gerenciamento de Pontos
                    <div className="select-container">
                        <select onChange={handleChange}>
                            {months.map((month, index) =>
                                <option key={index} value={month}>{month}</option>
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
                        <span className="point1-label">Ponto Entrada</span>
                        <span className="point2-label">Ponto Saida</span>
                    </li>
                    {marks.map((mark) =>
                        <li className="day">
                            <span className="point-body">{mark.markDate}</span>
                            <span className="point-body">{mark.markTime}</span>
                            <span className="point-body">{mark.type == 'ENTRY' ? 'Entrada' : 'Saida'}</span>
                            <button className="edit"> Editar </button>
                        </li>
                    )}
                </ul>
            </div>
            
        </>
    )
}