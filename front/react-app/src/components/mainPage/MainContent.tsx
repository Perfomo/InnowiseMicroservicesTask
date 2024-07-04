import { Button, Col, Row } from "antd";
import React from "react";

const MainContent: React.FC = () => {
  return (
    <main>
      <Row style={{ background: "sky-blue" }} justify="center">
        <Col span={12} style={{textAlign: "center"}}>
          <div className="card" style={{ border: "none", marginTop: "10%" }}>
            <div
              className="card-body"
              style={{
                textAlign: "center",
                fontWeight: "600",
                fontSize: "6vw",
                color: "rgba(0, 80, 255, 0.95)",
              }}
            >
              Welcome to the online shop!
            </div>
          </div>
          <Button
              type="primary"
              ghost
              style={{ width: "40%", height: "6vh", fontWeight: 700 }}
              href="/catalog"
            >
              Catalog
            </Button>
        </Col>
      </Row>
    </main>
  );
};

export default MainContent;
