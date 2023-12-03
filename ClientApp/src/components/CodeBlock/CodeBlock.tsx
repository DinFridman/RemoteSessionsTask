import { ListItemButton } from "@mui/material";
import { Client } from "@stomp/stompjs";
import confetti from "canvas-confetti";
import AceEditor from 'react-ace';
import { useEffect, useRef, useState } from "react";
import Highlight from "react-highlight";
import { Link, useLocation, useNavigate } from "react-router-dom";
import './CodeBlock.css';

function CodeBlock() {
    const location = useLocation()
    const codeBlock = location.state ? location.state.codeBlock : null;
    const [solutionMatched, setSolutionMatched] = useState(false);
    const [code, setCode] = useState("");
    const [role, setRole] = useState("");
    const [solution, setSolution] = useState("");
    const [showEffects, setShowEffects] = useState(false);
    const [terminateRemoteSession, setTerminateRemoteSession] = useState(false);
    const clientRef: React.MutableRefObject<any> = useRef(null);
    const roleRef = useRef("");
    const navigate = useNavigate();
    const initializedRef = useRef(false);

    useEffect(() => {
        if (!codeBlock) {
            navigate("/");
            return;
        }
    }, [codeBlock, navigate]);

    useEffect(() => {

        clientRef.current = new Client({
            brokerURL: "wss://remotesessionstask-production.up.railway.app/codeblocks-websocket",
        });
    
        clientRef.current.onConnect = () => {
            console.log('Connected');

            clientRef.current.subscribe(`/topic/remoteSessionDetails/${codeBlock.id}`, (message: any) => {

                const response = JSON.parse(message.body);
                switch(response.type) {
                    case("INIT_DATA") : {
                        if(initializedRef.current) {
                            return;
                        }
                        roleRef.current = response.role;
                        setRole(response.role);
                        setSolution(response.solution);
                        setCode(response.code);
                        break;
                    }

                    case("CODE_UPDATE") : {
                        if(role === "MENTOR") {
                            setCode(response.code);
                        }
                        break;
                    }

                    case("REMOTE_SESSION_TERMINATION") : {
                        setTerminateRemoteSession(response.isRemoteSessionTerminated);
                        break;
                    }
                    default:
                    console.log("Received an unknown message type:", response.type);
                }

                initializedRef.current = true;

            });
        };
    
        clientRef.current.onWebSocketError = (error: any) => {
            console.error('Error with websocket', error);
        };
    
        clientRef.current.onStompError = (frame: any) => {
            console.error('Broker reported error: ' + frame.headers['message']);
            console.error('Additional details: ' + frame.body);
        };

        
        clientRef.current.activate();
    
        return () => {
            clientRef.current.deactivate();
        };
    
    }, [codeBlock,]); 

    const handleDisconnection = () => {
    
        if (clientRef.current && clientRef.current.active) {
            clientRef.current.publish({
                destination: `/app/disconnectCodeBlock/${codeBlock.id}`,
                headers: { 'role-disconnection-header': roleRef.current },
                body: JSON.stringify({ "role": roleRef.current }),
            });
        }
    };
    
    const handleCodeChange = (newCode: any) => {
        clientRef.current.publish({
            destination: `/app/codeUpdate/${codeBlock.id}`,
            body: JSON.stringify({"code": newCode}),
        });

        
    };


    useEffect(() => {
        if (terminateRemoteSession ) {
            navigate("/"); 
        }
    }, [terminateRemoteSession, navigate]);


    const handleSolutionMatch = () => {
        setShowEffects(true);
        confetti({
            particleCount: 100,
            spread: 70,
            origin: { y: 0.6 }
        });
        setTimeout(() => setShowEffects(false), 500); 
    };

    useEffect(() => {
        if (code === solution && code !== "") {
            setSolutionMatched(true);
            handleSolutionMatch();
        }
        else {
            setSolutionMatched(false);
        }
    }, [code, solution]);

    const SmileyFace = () => (
        <div style={{ fontSize: '200px', textAlign: 'center' }}>
            😊 
        </div>
    );

  

    return (  
        <div className={`codeblock-container ${showEffects ? 'shake-animation' : ''}`}>
            <h1 className="codeblock-header">{codeBlock.title}</h1>
            <h2 className="codeblock-role">{"Role : " + role}</h2>
            {solutionMatched && <SmileyFace />}
            <ListItemButton 
                        component={Link} 
                        to={'/'}
                        onClick={() => handleDisconnection()}
                        sx={{
                        width: 'fit-content',
                        margin: 'auto',
                        backgroundColor: '#4a4a4a', 
                        border: '2px solid #ffffff',  
                        boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.2)',
                        color: 'white',   
                        padding: '10px 20px',
                        borderRadius: '8px', 
                        transition: 'all 0.3s', 
                        '&:hover': {
                            backgroundColor: '#b7b7b9',  
                            border: '2px solid #000000',
                            color: 'black',
                            boxShadow: '0px 0px 15px rgba(0, 0, 0, 0.3)'
                        }
                        }}
                    >
                    Go Back To Lobby
                    </ListItemButton>
            {role === "STUDENT" ? (
                <AceEditor 
                placeholder="Start Coding Here"
                mode="javascript"
                theme="tomorrow"
                name="codeblockEditor"
                onChange={handleCodeChange}
                fontSize={14}
                showPrintMargin={true}
                showGutter={true}
                highlightActiveLine={true}
                value={code}
                setOptions={{
                useWorker: false,
                showLineNumbers: true,
                tabSize: 2,
                }}
                className="codeblock-editor"/>
                          
            ) : (
                <div className='codeblock-mentor-container'>
                    <Highlight className="codeblock-editor">
                        {code}
                    </Highlight>
                </div>
                )
            }
        </div>
    );
}
export default CodeBlock;