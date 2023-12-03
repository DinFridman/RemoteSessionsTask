import { Link } from 'react-router-dom';
import './Lobby.css';

const codeBlocks = [
    { id: 0, title: "Async Case" },
    { id: 1, title: "JS Fundamentals"},
    { id: 2, title: "Functional Programming"},
    { id: 3, title: "Object Oriented Programming"}
];


function Lobby() {

    return (
      <div className='lobby-container'>
        <h1>Choose Code Block</h1>
        <ul>
          {codeBlocks.map((block, index) => (
            <li key={index}>
              <Link to={`/codeblock`} state={{ codeBlock: block }}>
                {block.title}
              </Link>
            </li>
          ))}
        </ul>
      </div>
    );
  }
  
  export default Lobby;
  