import { Button, Form, FormInstance, Input, InputNumber } from "antd";
import { useEffect, useState } from "react";


interface AllUserInfoFormProps {
  form: FormInstance;
  onFinish: (values: any) => void;
  buttonText: string;
}

const ProductInfoForm = ({
  form,
  onFinish,
  buttonText,
}: AllUserInfoFormProps) => {
  const [isButtonDisabled, setIsButtonDisabled] = useState<boolean>(true);

  useEffect(() => {
    const hasErrors = form.getFieldsError().some((field) => field.errors.length > 0);
    setIsButtonDisabled(hasErrors);
  }, [form]);

  const handleFieldsChange = () => {
    const hasErrors = form.getFieldsError().some((field) => field.errors.length > 0);
    setIsButtonDisabled(hasErrors);
  };
  
  return (
    <Form
      form={form}
      name="register"
      onFinish={onFinish}
      onFieldsChange={handleFieldsChange}
      style={{ maxWidth: 600 }}
      scrollToFirstError
    >
      <Form.Item
        name="name"
        rules={[
          {
            required: true,
            message: "Please input product name!",
          },
        ]}
      >
        <Input placeholder="Product Name" />
      </Form.Item>

      <Form.Item
        name="cost"
        rules={[
          {
            required: true,
            message: "Please input product cost!",
          },
          {
            type: "number",
            min: 0,
            max: 1000000,
            message: "Please enter a valid number!",
          },
        ]}
      >
        <InputNumber style={{width: "100%"}} placeholder="Product Cost" />
      </Form.Item>

      <Form.Item style={{ margin: "0%" }}>
        <Button type="primary" htmlType="submit" style={{ width: "100%" }}>
          {buttonText}
        </Button>
      </Form.Item>
    </Form>
  );
};

export default ProductInfoForm;
