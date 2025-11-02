import { useState } from 'react'
import './App.css'

function MarkForm(){
  const [mark, setMark] = useState("")

  const [type,setType] = useState("")

  function handleChange(e: any) {
    setMark(e.target.value)
  }

  function handleChangeType(e: any) {
    setType(e.target.value)
  }

  function handleSubmit(e: any) {
    e.preventDefault()
    console.log(mark,type)
    /*fetch('http://localhost:8080/api/v1/marks/mark',{
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        "markTime": "08:00:00",
        "markDate": "2025-03-03",
        "type": "ENTRY",
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
        .catch(err => console.log(err.message))*/
  }

  return(
    <div className='mark-card'>
      <form onSubmit={handleSubmit}>
        <div className='item-form'>
          <label>Ponto</label>
          <input type='datetime-local' value={mark} onChange={handleChange} name='mark' placeholder='Ponto de Entrada'/>
        </div>
        <div className='item-form'>
          <label>Tipo do Ponto </label>
          <select name='mark-type' onSelect={handleChangeType} className='mark-type'>
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
