import Navbar from "./Navbar";


const months = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 
                'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro']

export function ManageMarks(){
    return(
        <>
            <Navbar/>
            <div className="list-marks">
                <header>
                    Gerenciamento de Pontos
                    <div className="select-container">
                        <select>
                            {months.map((month) =>
                                <option>{month}</option>
                            )}
                        </select>
                        <select>
                            <option>2025</option>
                        </select>
                    </div>
                </header>
            </div>
            
        </>
    )
}