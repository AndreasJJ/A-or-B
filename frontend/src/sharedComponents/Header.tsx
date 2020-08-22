import * as React from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';

import { Button } from '../pages/create/Button';

const StyledHeader = styled.header`
    width: 100%;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background-color: #ffffff;
`;

const LogoWrapper = styled(Link)`
  height: fit-content;
  margin-right: 115px;
  margin-left: 25px;

  @media screen and (max-width: 1200px) {
      display: none;
  }
`;

const Logo = styled.h1`
    margin: 0;
    
    &:hover {
      color: cornflowerblue;
    }
`;

const Nav = styled.nav`
    display: flex;
    margin-right: 25px;
`;

const NavElement = styled(Link)`
    &:not(:last-child) {
      margin-right: 15px;
    }
`;

/**
 * Header component for the Dashboard
 */
export const Header: React.FC = () => {
    return (
        <StyledHeader>
            <LogoWrapper to="/">
                <Logo>A or B</Logo>
            </LogoWrapper>
            <Nav>
            <NavElement to="/game">
                <Button type="info" text="Join Game" />
              </NavElement>
              <NavElement to="/create">
                <Button type="info" text="My Games" />
              </NavElement>
              <NavElement to="/create/new">
                <Button type="success" text="Create Game" />
              </NavElement>
            </Nav>
        </StyledHeader>
    );
};
