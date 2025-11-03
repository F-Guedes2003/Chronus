import { useState } from "react";
import Navbar from "./Navbar";

type MarkType = 'ENTRY' | 'EXIT';

interface Mark {
  markTime: string;
  markDate: string;
  type: MarkType;
}

const months = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 
                'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro']

const days: string[] = []

for(let i = 1;i <= 31;i++){
    let day:string = i.toString()
    days.push(day.length < 2 ? '0'+ day : day)
}



export function ManageMarks(){

    const [marks, setMarks] = useState<Mark[]>([]);

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
            console.log(marks)
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
                            <span className="day-label">{mark.markDate}</span>
                            <span className="point1-label">{mark.type == 'ENTRY' ? mark.markTime : ''}</span>
                            <span className="point2-label">{mark.type == 'EXIT' ? mark.markTime : ''}</span>
                        </li>
                    )}
                </ul>
            </div>
            
        </>
    )
}