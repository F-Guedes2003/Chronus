import { Link } from "react-router-dom"

function Header() {
  return (
    <nav className='header'>
      <ul className="navbar">
        <Link to={"/manage-marks"} >
          <li><button className='header-button'>Gerenciamento de Ponto</button></li>
        </Link>
        <Link to={"/view-marks"}>
          <li><button className='header-button'>Relatório de Ponto</button></li>
        </Link>
      </ul>
    </nav>
  );
}

export default Header