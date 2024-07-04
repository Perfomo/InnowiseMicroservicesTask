import { ShoppingOutlined } from "@ant-design/icons";
const LogoButton = () => {
  return (
    <nav className="navbar bg-body-tertiary" style={{ marginLeft: "1%" }}>
      <div className="container-fluid">
        <a className="navbar-brand" href="/">
          <ShoppingOutlined />
          OnlineShop
        </a>
      </div>
    </nav>
  );
};

export default LogoButton;
