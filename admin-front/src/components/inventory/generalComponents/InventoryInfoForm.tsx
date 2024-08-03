import { Button, Form, FormInstance, Input, InputNumber } from "antd";
import { useEffect, useState } from "react";


interface AllInventoryInfoFormProps {
  form: FormInstance;
  onFinish: (values: any) => void;
  buttonText: string;
}

const InventoryInfoForm = ({
  form,
  onFinish,
  buttonText,
}: AllInventoryInfoFormProps) => {
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
      initialValues={{
        residence: ["zhejiang", "hangzhou", "xihu"],
        prefix: "86",
      }}
      style={{ maxWidth: 600 }}
      scrollToFirstError
    >
      <Form.Item
        name="name"
        rules={[
          {
            required: true,
            message: "Please input inventory name!",
          },
        ]}
      >
        <Input placeholder="Inventory Name" />
      </Form.Item>

      <Form.Item
        name="left"
        rules={[
          {
            required: true,
            message: "Please input inventory left!",
          },
          {
            type: "number",
            min: 0,
            max: 1000000,
            message: "Please enter a valid number!",
          },
        ]}
      >
        <InputNumber style={{width: "100%"}} placeholder="Inventory Left" />
      </Form.Item>

      <Form.Item
        name="sold"
        rules={[
          {
            required: true,
            message: "Please indicate the number of inventory sales!",
          },
          {
            type: "number",
            min: 0,
            max: 1000000,
            message: "Please enter a valid number!",
          },
        ]}
      >
        <InputNumber style={{width: "100%"}} placeholder="Inventory Sales" />
      </Form.Item>

      <Form.Item
        name="cost"
        rules={[
          {
            required: true,
            message: "Please input inventory cost!",
          },
          {
            type: "number",
            min: 0,
            max: 1000000,
            message: "Please enter a valid number!",
          },
        ]}
      >
        <InputNumber style={{width: "100%"}} placeholder="Inventory Cost" />
      </Form.Item>

      <Form.Item style={{ margin: "0%" }}>
        <Button type="primary" htmlType="submit" style={{ width: "100%" }}>
          {buttonText}
        </Button>
      </Form.Item>
    </Form>
  );
};

export default InventoryInfoForm;
