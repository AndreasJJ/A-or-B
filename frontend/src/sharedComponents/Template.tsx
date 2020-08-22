import React from 'react';
import styled from 'styled-components';
import { Header } from './Header';

const Wrapper = styled.div`
    display: flex;
    flex-direction: column;
    height: 100%;
    width: 100%;
    overflow: hidden;
`;

const Body = styled.div`
    height: Calc(100% - 60px);
    display: flex;
    position: relative;
`;

const Page = styled.div`
    flex: 1;
    background-color: #f2f2f2;
`;

interface DashboardProps {
    children: React.ReactNode;
}

/**
 * Component that wraps around the page components.
 * It's the general portal wrapper, with navigation and such.
 */
export const Template: React.FC<DashboardProps> = (props) => {
    return (
        <Wrapper>
            <Header />
            <Body>
                <Page>{props.children}</Page>
            </Body>
        </Wrapper>
    );
};
