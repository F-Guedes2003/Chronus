import { useState, type FormEvent } from 'react'
import './App.css'


// Tipos
type MarkType = 'ENTRY' | 'EXIT';

interface User {
  id: number;
}

interface Mark {
  user: User;
  markTime: string; // hora no formato HH:mm
  markDate: string; // data no formato YYYY-MM-DD
  type: MarkType;
}

interface HttpResponse<T> {
  status: number;
  message: string;
  data: T | null;
}

function MarkForm() {
  const [userId, setUserId] = useState<number | ''>('');
  const [markTime, setMarkTime] = useState('');
  const [markDate, setMarkDate] = useState('');
  const [markType, setMarkType] = useState<MarkType>('ENTRY');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    if (!userId || !markTime || !markDate) {
      setMessage('Todos os campos são obrigatórios!');
      return;
    }

    const payload: Mark = {
      user: { id: userId as number },
      markTime,
      markDate,
      type: markType,
    };

    try {
      console.log(payload)
      const response = await fetch('http://localhost:8080/api/v1/marks/mark', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      const data: HttpResponse<Mark> = await response.json();
      setMessage(`${data.status} - ${data.message}`);
    } catch (err) {
      console.error(err);
      setMessage('Erro ao enviar marcação!');
    }
  };

  return (
    <div className='form-container'>
      <h2>Registrar Ponto</h2>
      <form onSubmit={handleSubmit}>
        <label>
          User ID:
          <input
            type='number'
            value={userId}
            onChange={(e) => setUserId(e.target.value === '' ? '' : parseInt(e.target.value))}
          />
        </label>
        <label>
          Data da marcação:
          <input
            type='date'
            value={markDate}
            onChange={(e) => setMarkDate(e.target.value)}
          />
        </label>
        <label>
          Hora da marcação:
          <input
            type='time'
            value={markTime}
            onChange={(e) => setMarkTime(e.target.value)}
          />
        </label>
        <label>
          Tipo de ponto:
          <select
            value={markType}
            onChange={(e) => setMarkType(e.target.value as MarkType)}
          >
            <option value='ENTRY'>Entrada</option>
            <option value='EXIT'>Saída</option>
          </select>
        </label>
        <button type='submit'>Registrar</button>
      </form>
      {message && <p className='message'>{message}</p>}
    </div>
  );
}

export default MarkForm
