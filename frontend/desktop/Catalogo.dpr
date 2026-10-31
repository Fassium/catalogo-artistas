program Catalogo;

uses
  Vcl.Forms,
  View.Principal in 'src\principal\View.Principal.pas' {FrmPrincipal},
  Vcl.Themes,
  Vcl.Styles,
  System.UITypes,
  View.Login in 'src\autenticacao\View.Login.pas' {FrmLogin},
  Infra.Configuracao in 'src\infra\Infra.Configuracao.pas',
  Infra.Sessao in 'src\infra\Infra.Sessao.pas',
  Infra.Requisicao.Intf in 'src\infra\Infra.Requisicao.Intf.pas',
  Infra.Requisicao in 'src\infra\Infra.Requisicao.pas',
  Infra.Erros in 'src\infra\Infra.Erros.pas',
  Servico.Autenticacao in 'src\autenticacao\Servico.Autenticacao.pas';

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
