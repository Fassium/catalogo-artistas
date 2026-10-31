unit View.Login;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.StdCtrls;

type
  TFrmLogin = class(TForm)
    lblTitulo: TLabel;
    lblSubtitulo: TLabel;
    lblEmail: TLabel;
    lblSenha: TLabel;
    lblMensagemSenha: TLabel;
    edtEmail: TEdit;
    edtSenha: TEdit;
    chkMostrarSenha: TCheckBox;
    btnEntrar: TButton;
    procedure btnEntrarClick(Sender: TObject);
    procedure chkMostrarSenhaClick(Sender: TObject);
  private
    { Private declarations }
  protected
    // Dá ao login o próprio botão na barra de tarefas. Ele abre antes da janela principal existir
    // e, sem este ajuste, o Windows não mostraria o programa na barra.
    // WS_EX_APPWINDOW força o botão; WndParent := 0 solta o login da janela invisível do Application.
    procedure CreateParams(var Params: TCreateParams); override;
  public
    { Public declarations }
  end;

var
  FrmLogin: TFrmLogin;

implementation

{$R *.dfm}

{ TFrmLogin }

procedure TFrmLogin.btnEntrarClick(Sender: TObject);
begin
  lblMensagemSenha.Visible := False;

  if (edtEmail.Text = 'thiago') and (edtSenha.Text = '123') then
    ModalResult := mrOk
  else
  begin
    lblMensagemSenha.Caption := 'E-mail ou senha inválidos';
    lblMensagemSenha.Visible := True;
  end;
end;

procedure TFrmLogin.chkMostrarSenhaClick(Sender: TObject);
begin
  if chkMostrarSenha.Checked then
    edtSenha.PasswordChar := #0
  else
    edtSenha.PasswordChar := '*';
end;

procedure TFrmLogin.CreateParams(var Params: TCreateParams);
begin
  inherited;
  Params.ExStyle := Params.ExStyle or WS_EX_APPWINDOW;
  Params.WndParent := 0;
end;

end.
