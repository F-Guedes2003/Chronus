import { useState } from 'react'
import './App.css'

function MarkForm(){
  const [markDate, setMarkDate] = useState("")

  const [markTime,setMarkTime] = useState("")

  const [type,setType] = useState("ENTRY")

  const changeMarkTime = (e: any) => {
    setMarkTime(e.target.value)
  }

  const changeMarkDate = (e: any) => {
    setMarkDate(e.target.value)
  }

  const changeType = (e: any) => {
    setType(e.target.value.toUpperCase())
  }

  function handleSubmit(e: any) {
    e.preventDefault()
    console.log(markTime,markDate,type)
    fetch('http://localhost:8080/api/v1/marks/mark',{
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        "markTime": markTime,
        "markDate": markDate,
        "type": type,
        "valid": "true",
        "user": {
            "id": 28,
            "name": "Vitor Roque",
            "email": "vitinbates@sep.com",
            "password": "password"
        }
      })
      }).then(response => response.text())
        .then(text => console.log(text))
        .catch(err => console.log(err.message))
  }

  return(
    <div className='mark-card'>
      <form onSubmit={handleSubmit}>
        <div className='item-form'>
          <label>Ponto</label>
          <input className='input-form' type='date' value={markDate} onChange={changeMarkDate} name='mark'/>
          <input className='input-form' type='time' value={markTime} onChange={changeMarkTime} name='mark'/>
        </div>
        <div className='item-form'>
          <label>Tipo do Ponto </label>
          <select name='mark-type' onChange={changeType} className='mark-type'>
            <option value="entry">Entrada</option>
            <option value="exit">Saida</option>
          </select>
        </div>
        <button className='bt-submit' type='submit' name='submit-bt'>Enviar Ponto</button>
      </form>
    </div>
  )
}

export default MarkForm
