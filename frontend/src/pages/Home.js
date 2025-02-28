import React from "react";
import classNames from "classnames";
import { Link } from "react-router-dom";
import axios from 'axios';
import '../styles/css/index.css'

import {setCookie, getCookie, removeCookie} from 'components/Cookie.js';

// @material-ui/core components
import { makeStyles } from "@material-ui/core/styles";
import Alert from '@material-ui/lab/Alert';
// @material-ui/icons
// core components
import Header from "components/Header/Header.js";
import Footer from "components/Footer/Footer.js";
import GridContainer from "components/Grid/GridContainer.js";
import GridItem from "components/Grid/GridItem.js";
import Button from "components/CustomButtons/Button.js";
import Parallax from "components/Parallax/Parallax.js";
// sections for this page
import HeaderLinks from "components/Header/HeaderLinks.js";
import SectionBasics from "./AdminPage/Sections/SectionBasics.js";
import SectionNavbars from "./AdminPage/Sections/SectionNavbars.js";
import SectionTabs from "./AdminPage/Sections/SectionTabs.js";
import SectionPills from "./AdminPage/Sections/SectionPills.js";
import SectionNotifications from "./AdminPage/Sections/SectionNotifications.js";
import SectionTypography from "./AdminPage/Sections/SectionTypography.js";
import SectionJavascript from "./AdminPage/Sections/SectionJavascript.js";
import SectionCarousel from "./AdminPage/Sections/SectionCarousel.js";
import SectionCompletedExamples from "./AdminPage/Sections/SectionCompletedExamples.js";
import SectionLogin from "./AdminPage/Sections/SectionLogin.js";
import SectionExamples from "./AdminPage/Sections/SectionExamples.js";
import SectionDownload from "./AdminPage/Sections/SectionDownload.js";

import styles from "assets/jss/material-kit-react/views/components.js";

const useStyles = makeStyles(styles);


export default function Components(props) {
  const [loginStatus, setLoginStatus] = React.useState(getCookie('access-token')===undefined? false:true);
  const [username, setUsername] = React.useState(getCookie('user-name')===undefined? "":getCookie('user-name'));
  console.log(loginStatus)
  const classes = useStyles();
  const { ...rest } = props;

  const aiServerTest = () => {
    const api = axios.create({
      baseURL: 'http://168.188.125.50:20017'
    })
    api.get('/', null, {
      //params
    }).then(function (response) {
      console.log(response.data)
      alert("projectId : "+response.data.projectId+"\nprojectType : "+response.data.projectType);
    }).catch(function (error) {
      console.log(error);
    });
  }

  return (
    <div>
      <Header
        username={username}
        brand="Easy AI"
        rightLinks={<HeaderLinks onLogout={
          function(_loginStatus){
            setLoginStatus(_loginStatus)

            /*remove cookie*/
            removeCookie('access-token');
            removeCookie('user-name');
            //remove local Storage refresh token
            localStorage.removeItem("refresh-token");
            setUsername("")
          }}
          loginStatus={loginStatus}/>}
        fixed
        color="dark"
        changeColorOnScroll={{
          height: 400,
          color: "info",
        }}
        {...rest}
      />
      <Parallax
      image={require("assets/img/workers-5246640_1920.jpg").default}>
        <div className={classes.container}>
          <GridContainer>
            <GridItem>
              <div className={classes.brand}>
                <h1 className={classes.title}>Easy AI</h1>
                <h3 className={classes.subtitle}>
                  클릭만으로 자신만의 AI를 만들어보세요
                </h3>
                
                <Link to={"/admin"} className={classes.link}>
                  <Button color="info">
                    <h4><strong>지금 바로 AI 만들기</strong></h4>
                  </Button>
                </Link>
                <br></br>
                {/*<Button onClick={aiServerTest}>
                    <h4><strong>AI 서버 테스트</strong></h4>
                </Button>*/}
              </div>
            </GridItem>
          </GridContainer>
        </div>
      </Parallax>

      <div className={classNames(classes.main, classes.mainRaised)}>
        <SectionTabs />
        <SectionBasics />
        <SectionNavbars />
        <SectionPills />
        <SectionNotifications />
        <SectionTypography />
        <SectionJavascript />
        <SectionCarousel />
        <SectionCompletedExamples />
        <SectionLogin />
        <GridItem md={12} className={classes.textCenter}>
          <Link to={"/login-page"} className={classes.link}>
            <Button color="primary" size="lg" simple>
              View Login Page
            </Button>
          </Link>
        </GridItem>
        <SectionExamples />
        <SectionDownload />
      </div>
      <Footer />
    </div>
  );
}
