import { useMemo, useState } from "react";
import Navbar from "./Navbar";
import Popup from "reactjs-popup";

type MarkType = 'ENTRY' | 'EXIT';

interface Mark {
  markTime: string;
  markDate: string;
  type: MarkType;
}

interface GroupedMark {
    markDate: string;
    entryTime: string;
    exitTime: string;
}

const months = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 
                'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro']

export function ManageMarks(){

    const [marks, setMarks] = useState<Mark[]>([]);

    const groupedMarks = useMemo(() => {
        const groups = new Map<string, GroupedMark>();

        marks.forEach(mark => {
            const date = mark.markDate;
            
            if (!groups.has(date)) {
                groups.set(date, {
                    markDate: date,
                    entryTime: '',
                    exitTime: ''
                });
            }

            const currentGroup = groups.get(date)!;

            if (mark.type === 'ENTRY') {
                currentGroup.entryTime = currentGroup.entryTime ? `${currentGroup.entryTime}, ${mark.markTime}` : mark.markTime;
            } else if (mark.type === 'EXIT') {
                currentGroup.exitTime = currentGroup.exitTime ? `${currentGroup.exitTime}, ${mark.markTime}` : mark.markTime;
            }
        });
        return Array.from(groups.values());
    }, [marks]);

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
            console.log(data)
        } catch (err) {
            console.error(err);
        } 
    }

    const handleEdit = async(e: GroupedMark) => {

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
                    {groupedMarks.map((group) =>
                        <li className="day">
                            <span className="point-body">{group.markDate}</span>
                            <span className="point-body">{group.entryTime}</span>
                            <span className="point-body">{group.exitTime}</span>
                            <Popup trigger={<button className="edit"> Editar </button>} modal>¨
                                <div className="modal">
                                    <input type="text" value={group.markDate}/>
                                    <input type="text" value={group.entryTime}/>
                                    <input type="text" value={group.exitTime}/>
                                    <button onClick={() => handleEdit(group)} className="edit">Salvar</button>
                                </div>
                            </Popup>
                        </li>
                    )}
                </ul>
            </div>
            
        </>
    )
}