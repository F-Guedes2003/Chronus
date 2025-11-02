import './App.css'
import avatar from './assets/default-avatar-2.png'

function Header(){
  return(
    <div className='header'>
      <ul>
        <li className='title'>Chronus</li>
        <li><button className='header-button'>Gerenciamento de Ponto</button></li>
        <li><button className='header-button'>Relatório de Ponto</button></li>
        <img className='avatar' src={avatar}/>
      </ul>
    </div>
  )
}

export default Header
