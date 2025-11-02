import { useState } from 'react'
import './App.css'

function MarkForm(){
  const [entry, setEntry] = useState("")
  const [exit, setExit] = useState("")

  function handleChangeEntry(e: any) {
    setEntry(e.target.value)
  }

  function handleChangeExit(e: any){
    setExit(e.target.value)
  }

  function handleSubmit(e: any) {
    e.preventDefault()
    console.log(entry,exit)
  }

  return(
    <div className='mark-card'>
      <form onSubmit={handleSubmit}>
        <div className='item-form'>
          <label>Ponto de Entrada</label>
          <input type='datetime-local' value={entry} onChange={handleChangeEntry} name='entry-mark' placeholder='Ponto de Entrada'/>
        </div>
        <div className='item-form'>
          <label>Ponto de Saida </label>
          <input type='datetime-local' value={exit} onChange={handleChangeExit} name='exit-mark' placeholder='Ponto de Saida'/>
        </div>
        <button className='bt-submit' type='submit' name='submit-bt'>Enviar Ponto</button>
      </form>
    </div>
  )
}

export default MarkForm
