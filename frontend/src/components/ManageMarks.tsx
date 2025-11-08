import {use, useState, type FormEvent } from "react";
import Navbar from "./Navbar";
import Popup from "reactjs-popup";

type MarkType = 'ENTRY' | 'EXIT';

interface User {
  id: number;
}

interface HttpResponse<T> {
  status: number;
  message: string;
  data: T | null;
}

interface Mark {
  id: string;  
  user: User;
  markTime: string;
  markDate: string;
  type: MarkType;
}

const months = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 
                'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro']

export function ManageMarks(){

    const [id, setId] = useState('');
    const [userId, setUserId] = useState<number | ''>(34);
    const [marks, setMarks] = useState<Mark[]>([]);
    const [mark, setMark] = useState<Mark>();
    const [markTime, setMarkTime] = useState('');
    const [markDate, setMarkDate] = useState('');
    const [markType,setMarkType] = useState<MarkType>('ENTRY');
    const [message, setMessage] = useState('');

    const handleEdit = async(e: FormEvent) => {
        e.preventDefault()

    if (!markTime || !markDate) {
      setMessage('Todos os campos são obrigatórios!');
      return;
    }

    const payload: Mark = {
        user: { id: userId as number },
        markTime,
        markDate,
        type: markType,
        id
    };

    try {
      console.log(payload)
      const response = await fetch(`http://localhost:8080/api/v1/marks/mark/${payload.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      const data: HttpResponse<Mark> = await response.json();
      setMessage(`${data.status} - ${data.message}`);
    } catch (err) {
      console.error(err);
      setMessage('Erro ao enviar marcação!');
    }
    }

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
                        <select className="select-month" onChange={handleChange}>
                            {months.map((month, index) =>
                                <option key={index} value={month}>{month}</option>
                            )}
                        </select>
                        <select className="select-month">
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
                            <Popup trigger={<button className="edit"> Editar </button>} modal>
                                <div className="modal">
                                    <form onSubmit={handleEdit}>
                                        <input type="date" onChange={(e) => setMarkDate(e.target.value)}/>
                                        <input type="time" onChange={(e) => setMarkTime(e.target.value)}/>
                                        <select
                                            value={markType}
                                            onChange={(e) => setMarkType(e.target.value as MarkType)}
                                        >
                                            <option value='ENTRY'>Entrada</option>
                                            <option value='EXIT'>Saída</option>
                                        </select>
                                        <button onClick={(e) => setId(mark.id)}  type="submit" className="edit"> Salvar </button>
                                    </form>
                                </div>
                            </Popup>
                        </li>
                    )}
                </ul>
            </div>
            
        </>
    )
}