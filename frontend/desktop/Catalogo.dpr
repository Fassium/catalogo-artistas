program Catalogo;

uses
  Vcl.Forms,
  View.Principal in 'src\principal\View.Principal.pas' {FrmPrincipal},
  Vcl.Themes,
  Vcl.Styles,
  System.UITypes,
  View.Login in 'src\autenticacao\View.Login.pas' {FrmLogin};

{$R *.res}

begin
  Application.Initialize;
  Application.MainFormOnTaskbar := True;
  TStyleManager.TrySetStyle('Windows10');

  FrmLogin := TFrmLogin.Create(nil);
  try
    if FrmLogin.ShowModal <> mrOk then
      Exit;
  finally
    FrmLogin.Free;
  end;

  Application.CreateForm(TFrmPrincipal, FrmPrincipal);
  Application.Run;
end.
