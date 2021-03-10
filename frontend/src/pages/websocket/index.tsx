import React, { useState, useRef } from 'react';
import styled from 'styled-components';
import { useKeycloak } from '@react-keycloak/web';

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
`;

const Log = styled.div`
  white-space: pre-line;
  width: 20em;
  height: 15em;
  overflow: auto;
  border: 1px solid black;
`;

const Websocket: React.FC = () => {
  const { keycloak } = useKeycloak();
  const conn = useRef<WebSocket | null>(null);

  const [inputRef, setInputFocus] = useFocus();
  const textRef = useRef<HTMLDivElement | null>(null);

  const [buttonText, setButtonText] = useState('Connect');
  const [status, setStatus] = useState('Disconnected');
  const [logHtml, setLogHtml] = useState('');

  const log = (msg: string) => {
    setLogHtml((prev) => (`${prev + msg}\n`));
  };

  const getTicket = async () => {
    const res = await fetch('/api/ticket', {
      method: 'GET',
      mode: 'cors',
      cache: 'no-cache',
      credentials: 'same-origin',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${keycloak.token}`,
      },
      redirect: 'follow',
      referrerPolicy: 'no-referrer',
    });

    const ticket = await res.json();
    return ticket;
  };

  const updateUi = () => {
    if (conn.current == null) {
      setStatus('Disconnected');
      setButtonText('Connect');
    } else {
      setStatus(`Connected (${conn.current.protocol})`);
      setButtonText('Disconnect');
    }
  };

  const disconnect = () => {
    if (conn.current != null) {
      log('Disconnecting...');
      conn.current.close();
      conn.current = null;
      updateUi();
    }
  };

  const connect = async () => {
    const ticket = await getTicket();

    disconnect();

    const wsUri = `${((window.location.protocol === 'https:' && 'wss://') || 'ws://')}${window.location.host}/api/ws/game/1`;

    conn.current = new WebSocket(wsUri);

    log('Connecting...');

    conn.current.onopen = () => {
      log('Connected.');
      updateUi();
    };

    conn.current.onmessage = (e) => {
      log(`Received: ${e.data}`);
    };

    conn.current.onclose = () => {
      log('Disconnected.');
      conn.current = null;
      updateUi();
    };
  };

  const onConnectClick = () => {
    if (conn.current == null) {
      connect();
    } else {
      disconnect();
    }
    updateUi();
    return false;
  };

  const onSendClick = () => {
    if (conn.current && inputRef.current) {
      const text = inputRef.current.value;
      log(`Sending: ${text}`);
      const message = {
        action: "STARTGAME",
        text: "Hello"
      }
      conn.current.send(JSON.stringify(message));
      setInputFocus();
    }
    return false;
  };

  const onTextKeyUp = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.keyCode === 13) {
      onSendClick();
    }
  };

  return (
    <Wrapper>
      <Content>
        <h3>Chat!</h3>
        <div>
          <button type="submit" onClick={onConnectClick}>{buttonText}</button>
          &nbsp;|&nbsp;Status:
          <span>{status}</span>
        </div>
        <Log ref={textRef}>
          {logHtml}
        </Log>
        <form onSubmit={() => false}>
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
