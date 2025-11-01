import { useState } from 'react'
import './App.css'

function Header(){
  return(
    <div className='header'>
      <ul>
        <li><button className='header-button'>Gerenciamento de Ponto</button></li>
        <li><button className='header-button'>Relatório de Ponto</button></li>
      </ul>
    </div>
  )
}


function App() {
  return (
    <Header/>
  )
}

export default App
