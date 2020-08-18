import React, { useState, useRef } from 'react';
import styled from 'styled-components';

import { useFocus } from '../../hooks';

const Wrapper = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  align-content: center;
  justify-content: center;
  justify-items: center;
  background-color: #f5f5f5;
  height: 100%;
  width: 100%;
`;

const Content = styled.div`
  background-color: #ffffff;
  padding: 15px;
  -webkit-box-shadow: 0 0.0625em 0.125em rgba(0,0,0,0.15);
  -moz-box-shadow: 0 0.0625em 0.125em rgba(0,0,0,0.15);
  box-shadow: 0 0.0625em 0.125em rgba(0,0,0,0.15);
`

const Log = styled.div`
  white-space: pre-line;
  width: 20em;
  height: 15em;
  overflow: auto;
  border: 1px solid black;
`;

const Websocket: React.FC = () => {
  let conn = useRef<WebSocket | null>(null);

  const [inputRef, setInputFocus] = useFocus();
  const textRef = useRef<HTMLDivElement | null>(null);

  const [buttonText, setButtonText] = useState("Connect");
  const [status, setStatus] = useState("Disconnected");
  const [logHtml, setLogHtml] = useState("");
  const [logScroll, setLogScroll] = useState(0);

  const log = (msg: string) => {
    setLogHtml(prev => (prev + msg + '\n'));
    //control.html(control.html() + msg + '<br/>');
    if (textRef.current) {
      setLogScroll(textRef.current.scrollTop + 1000)
      //control.scrollTop(control.scrollTop() + 1000);
    }
    
  }

  const connect = () => {
    disconnect();

    let wsUri = (window.location.protocol=='https:'&&'wss://'||'ws://') + window.location.host + '/api/websocket/';

    conn.current = new WebSocket(wsUri);

    log('Connecting...');

    conn.current.onopen = () => {
      log('Connected.');
      update_ui();
    };

    conn.current.onmessage = (e) => {
      log('Received: ' + e.data);
    };

    conn.current.onclose = () => {
      log('Disconnected.');
      conn.current = null;
      update_ui();
    };
  }

  const disconnect = () => {
    if (conn.current != null) {
      log('Disconnecting...');
      conn.current.close();
      conn.current = null;
      update_ui();
    }
  }

  const update_ui = () => {
    let msg = '';
    if (conn.current == null) {
      setStatus("Disconnected")
      setButtonText("Connect")
    } else {
      setStatus("Connected (" + conn.current.protocol + ")");
      setButtonText("Disconnect")
    }
  }

  const onConnectClick = () => {
    if (conn.current == null) {
      connect();
    } else {
      disconnect();
    }
    update_ui();
    return false;
  }

  const onSendClick = () => {
    if (conn.current && inputRef.current) {
      var text = inputRef.current.value;
      log('Sending: ' + text);
      conn.current.send(text);
      setInputFocus;
    }
    return false;
  }

  const onTextKeyUp = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.keyCode === 13) {
      onSendClick();
      return false;
    }
  }

  return (
    <Wrapper>
      <Content>
        <h3>Chat!</h3>
        <div>
          <button onClick={onConnectClick}>{buttonText}</button>
          &nbsp;|&nbsp;Status:
          <span>{status}</span>
        </div>
        <Log ref={textRef}>
          {logHtml}
        </Log>
        <form onSubmit={() => {return false;}}>
          <input onKeyUp={onTextKeyUp} ref={inputRef} type="text" />
          <input onClick={onSendClick} type="button" value="Send" />
        </form>
      </Content>
    </Wrapper>
  );
};

export {
  Websocket,
};
