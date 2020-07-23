// <%@ page contentType="text/html;charset=UTF-8" language="java" %>
// <%@include file="/WEB-INF/jsp/common/taglibs.jspf" %>
// <!doctype html>
// <html>
// <head>
	// <meta http-equiv="Content-Type" charset="UTF-8" />
	// <meta http-equiv="Cache-Control" content="no-cache" />
	// <title>捞财宝投资协议</title>
	// <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0, minimal-ui"/>
	// <meta name="apple-mobile-web-app-capable" content="yes" />
	// <meta name="apple-mobile-web-app-status-bar-style" content="black" />
	// <meta name="format-detection" content="telephone=no" />
	// <meta name="format-detection" content="email=no" />
	// <meta name="keywords" content="" />
	// <meta name="description" content="" />
	// <link href="${ctx}/static/h5/css/zdmoney.min.css" rel="stylesheet" type="text/css"/>
	// <style type="text/css">
	// p,ul{ margin: 0; padding: 0;}
	// em,i{ font-style: normal;}
	// ul li{list-style: none;padding:0; margin:0;}
	// img{border:0 none ;vertical-align:bottom;}
	// .banner{line-height:0; font-size:0; overflow:hidden;}
	// .page{background: #fff;}
	// .left{float:left}
	// .right{float:right;}
	// .clear:after{content:'';clear:both;display:block;visibility: hidden;height:0px; width:0px;font-size:0px;}
	// .clear{zoom:1}
	// .agreePage{padding-left: 10px; padding-right: 10px;}
	// .agreePage .mt10{margin-top: 10px;}
	// .agreePage .title{ height: 40px; line-height: 40px; font-size: 20px; font-weight: bold; color: #000; text-align: center;}
	// .agreePage .mytable{}
	// table.gridtable {
	   // width: 100%;
	   // text-align: left;
	   // color:#222;
	   // border-collapse: collapse;
	   // }
	// table.gridtable th {
	   // color: #fff;
	   // border-width: 1px;
	   // padding: 8px;
	   // border-style: solid;
	   // border-color: #d7d7d7;
	   // background-color: #ff6239;
	   // }
	// table.gridtable td {
	   // border-width: 1px;
	   // padding: 8px;
	   // border-style: solid;
	   // border-color: #d7d7d7;
	   // background-color: #ffffff;
	   // }
	// .agreePage .ftb{font-size: 16px; font-weight: bold;}
	// </style>
	// </head>
// <body>
// <div class="views">
	// <div class="view view-main">
	// <div class="pages">
	// <div class="page" data-page="index">
	// <div class="page-content">
	// <div class="agreePage">
	// <p class="title">投资协议</p>
	// <p>订单编号：</p>
	// <p>${result.msgEx.infos.subjectInvestAgreement.orderNum}</p>
	// <p class="mt10">尊敬的投资客户：为了维护您的自身权益、防范投资风险，请在投资前仔细阅读本协议各条款（尤其是风险提示）、以及与本产品相关的全部产品规则，特别是加粗字体条款，以充分知悉、了解捞财宝产品的运作规则、投资范围以及协议双方的权利、义务和责任。一旦加入本投资计划即视为对本协议全部条款及相关业务规则已充分理解并完全接受。</p>
	// <p class="mt10">甲方：上海证大爱特金融信息服务有限公司</p>
	// <p class="mt10">注：甲方具有提供互联网信息服务的资质并拥有捞财宝（www.laocaibao.com）的经营权。</p>
	// <p>地址：上海市浦东新区峨山路91弄120号陆家嘴软件园8号楼7楼</p>
	// <p class="mt10">
	// 乙方：${result.msgEx.infos.subjectInvestAgreement.customerName}<br/>
	// 认证姓名：${result.msgEx.infos.subjectInvestAgreement.customerName}<br/>
	// 身份证号码：${result.msgEx.infos.subjectInvestAgreement.investIdNum}<br/>
	// </p>
	// <p class="mt10">投资信息详情：</p>
	// <div class="mytable">
	// <table class="gridtable">
	// <tr>
	// <td style="width: 48%">产品名称</td>
	// <td>${result.msgEx.infos.subjectInvestAgreement.productName}</td>
	// </tr>
	// <tr>
	// <td>借款人</td>
	// <td>${result.msgEx.infos.subjectInvestAgreement.borrowName}</td>
	// </tr>
	// <tr>
	// <td>身份证号/营业执照号</td>
	// <td>${result.msgEx.infos.subjectInvestAgreement.borrowerIdNo}</td>
	// </tr>
	// <tr>
	// <td>借款用途</td>
	// <td>${result.msgEx.infos.subjectInvestAgreement.borrowPurpose}</td>
	// </tr>
	// <tr>
	// <td>加入金额(元)</td>
	// <td>${result.msgEx.infos.subjectInvestAgreement.orderAmt}</td>
	// </tr>
	// <tr>
	// <td>预期年化收益率</td>
	// <td>${result.msgEx.infos.subjectInvestAgreement.yearRate}</td>
	// </tr>
	// <tr>
	// <td>预计到期本息(元)</td>
	// <td>${result.msgEx.infos.subjectInvestAgreement.principalInterest}</td>
	// </tr>
	// <tr>
	// <td>购买时间</td>
	// <td>${result.msgEx.infos.subjectInvestAgreement.orderTime}</td>
	// </tr>
	// <tr>
	// <td>起息日期</td>
	// <td>${result.msgEx.infos.subjectInvestAgreement.interestStartDate}</td>
	// </tr>
	// <tr>
	// <td>到期日期</td>
	// <td>${result.msgEx.infos.subjectInvestAgreement.interestEndDate}</td>
	// </tr>
	// </table>
	// </div>
	// <p class="mt10" style="text-indent:30px">
	// 在乙方充分了解并清楚知晓本产品投资风险的前提下，就乙方委托甲方将其资金进行分散出借以及申购（以下简称“本投资计划”）相关事项，双方达成如下协议：
	// </p>
	// <p class="mt10 ftb">风险提示：</p>
	// <p class="mt10">1. 投资风险：乙方加入本投资计划后，可通过分散投资方式降低单一借款项目所带来的投资风险，但本投资计划不同于银行储蓄，乙方既可能分享投资所产生的收益，也可能承担投资所带来的损失。乙方应判断本投资计划与其自身风险承受能力、其资产管理需求是否相匹配后，自行决定是否加入本投资计划。</p>
	// <p class="mt10">2. 市场风险：甲方承诺将基于诚实信用、勤勉尽责的原则协助乙方将资金出借给借款客户，但本投资计划在运作过程中可能面临各种市场风险，甲方并不保证本投资计划一定会产生盈利，亦不保证最低收益。</p>
	// <p class="mt10">3. 政策风险：本投资计划依据现行相关法律法规和政策设计，如相关法律法规、政策等发生变化，可能影响本投资计划的正常进行，甚至终止本投资计划。</p>
	// <p class="mt10">4. 信息传递风险：本投资计划存续期内，甲方可根据本协议约定对本协议及相关规则进行调整，甚至终止本投资计划。届时，甲方将在捞财宝（包括但不限于官网公告、站内信、手机平台等方式）进行公布。乙方应根据本协议所载的公告方式及时查询相关信息，如因乙方未及时查询，或通讯故障、系统故障以及其他不可抗力等因素的影响使得乙方未能及时了解到相关调整，由此产生的责任和风险由乙方自行承担。</p>
	// <p class="mt10">5.不可抗力风险 : 由于自然灾害、战争社会动乱、罢工等不可抗力因素的出现，将严重影响本投资计划的正常运行。</p>
	// <p class="mt10 ftb">释义：</p>
	// <p class="ftb">除非本协议另有规定，以下词语在本协议中定义如下：</p>
	// <p class="mt10">a.捞财宝：指由甲方运营和管理的网站，域名为：www.laocaibao.com。</p>
	// <p class="mt10">b. 出借人（乙方）：指通过甲方捞财宝成功注册账户的会员，可参考甲方的投资计划自主选择出借一定金额的资金给借款客户，且具有完全民事行为能力的自然人。</p>
	// <p class="mt10">c. 合作方：指与甲方建立合作关系的机构，包括但不限于小额贷款公司、融资性担保公司、第三方支付机构、基金管理公司等。</p>
	// <p class="mt10">d. 借款客户：指有一定的资金需求，向甲方合作方借款，且具有完全民事权利/行为能力的自然人。</p>
	// <p class="mt10">e. 借款：出借人拟向甲方合作方借款客户提供的借款。</p>
	// <p class="mt10">f. 主账户：指出借人或借款客户以自身名义在捞财宝注册后系统自动产生的虚拟账户，通过第三方支付机构及/或其他通道进行充值或提现。</p>
	// <p class="mt10">g. 担保：指合作方为出借人的借款提供的全额本息保障方式，包括但不限于保证、抵押、质押等方式，或承诺进行代偿、债权回购、债权收购或发放后备贷款等方式。</p>
	// <p class="mt10">h. 加入资金：指出借人加入到本投资计划的资金，具体包括但不限于出借给借款客户的资金。</p>
	// <p class="mt10">i. 债权：指乙方通过向借款客户投入资金而在《投资协议》项下享有的所有权利。</p>
	// <p class="mt10 ftb">一、主要内容</p>
	// <p class="mt10">1.1 产品介绍：甲方根据乙方的委托，对合作方推荐的产品（包括但不限于借款项目）进行分散筛选、协助乙方将加入资金出借给借款客户、自动转让的投资计划。在满足本协议相关规则的前提下，出借人的投入资金及投入资金所产生的收益，到期后会自动划转至出借人绑定的银行账户。</p>
	// <p class="mt10">1.2 投资范围：本投资计划的投资范围包含经合作方提供的借款项目（包括原始借款项目及债权转让项目，下同）。</p>
	// <p class="mt10">1.3 预期收益：乙方知悉、了解并同意，捞财宝宣传页面显示的与本投资计划相关的收益均为预期收益，甲方未以任何方式对加入资金的本金及预期收益进行承诺或担保，乙方的加入资金存在不能够全额收回的风险；同时，在实际收益未达到预期收益的情况下，乙方仅能收取其当笔加入资金所对应的实际收益。</p>
	// <p class="mt10">1.4 收益结算：本协议项下的加入资金按日计息，一次性还本付息。本投资计划的收益以甲方结息日实际结算为准，收益将在乙方主账户中显示。</p>
	// <p class="mt10">1.5 加入资金来源保证：乙方保证其在本协议项下的加入资金来源合法，乙方是该加入资金的合法所有人。如果第三方对加入资金归属、合法性问题提出异议，由乙方自行解决。如未能解决，则乙方承诺放弃享有加入资金带来的收益。</p>
	// <p class="mt10">1.6 乙方在本投资计划中的加入资金总额、变动情况、收益等以乙方主账户记录为准。</p>
	// <p class="mt10 ftb">二、本协议的成立及生效</p>
	// <p class="mt10">2.1 本协议成立：乙方按照捞财宝的规则，通过在捞财宝上点击“立即抢购”按钮（具体以应用显示为准），即视为乙方与甲方已达成协议并同意接受本协议的全部约定以及捞财宝应用所包含的其他与本协议有关的各项规则的规定。</p>
	// <p class="mt10">2.2 加入资金冻结：乙方点击“立即抢购”按钮（具体以应用显示为准）确认后，即视为乙方已经向甲方发出不可撤销的授权指令，授权甲方委托相应的第三方支付机构及/或甲方开立监管账户的监管银行（“监管银行”）等合作方，在乙方主账户中，冻结金额等同于乙方当笔加入资金数额。上述冻结在甲方为乙方进行当笔加入资金的划转时解除。</p>
	// <p class="mt10">2.3 资金划转：</p>
	// <p class="mt10">2.3.1 当笔加入资金全部冻结，且甲方系统完成相应资金的募集后，根据所投特定项目的《债权转让协议》的相关约定，上述特定项目的借款客户即不可撤销地授权甲方委托相应的第三方支付机构及/或监管银行等合作方，将当笔加入资金由监管账户划转至对应的借款客户指定的银行账户。</p>
	// <p class="mt10">2.3.2 甲方将在起息日前完成该笔加入资金的统一划转。</p>
	// <p class="mt10">2.4 本协议生效：本协议于甲方完成乙方首次加入资金的划转之日立即生效，本协议生效后对乙方后续操作（包括但不限于再次加入资金、赎回资金等）具有约束力，无须反复签订协议。</p>
	// <p class="mt10 ftb">三、投资及资产管理</p>
	// <p class="mt10">3.1 乙方全权委托甲方按照本协议的约定，对加入资金出借给借款客户；同时，乙方授权甲方在完成上述申购及出借后以乙方名义代为签署相应的《债权转让协议》。</p>
	// <p class="mt10">3.2 投资及资产管理规则：</p>
	// <p class="mt10">3.2.1 乙方加入本服务的资金按日计息，一次性还本付息。</p>
	// <p class="mt10">3.2.2 为尽力达到预期年化收益率，在乙方加入本投资计划后、退出前，包括但不限于加入资金、赎回资金、收益复投及为调整乙方在本投资计划中全部加入资金投资于各类资产的比例而进行的资金投资分配时，乙方不可撤销地委托并授权甲方通过其系统按一定比例自动配置投资于各类资产的资金，包含但不限于对借款项目的购买及转让等操作。</p>
	// <p class="mt10">3.2.3 甲方按本协议约定提供给乙方投资的借款项目不包括已出现逾期还款且合作方未立即履行相应担保义务的借款项目。</p>
	// <p class="mt10">3.2.4 乙方在此明确同意并授权甲方，为尽力维护乙方利益，不时地调整乙方所投资产品中的借款项目。例如，若甲方按本协议约定提供给乙方投资的借款项目，在乙方持有该借款项目期间出现逾期还款且合作方未立即履行相应担保义务，则甲方可自主决定将此借款项目退出本投资产品，并按照需要在投资产品中补充新的借款项目。</p>
	// <p class="mt10">3.3 乙方加入本投资计划后，甲方将按照乙方加入本投资计划的先后顺序，协助乙方将加入资金出借给借款客户。</p>
	// <p class="mt10 ftb">四、收益及费用</p>
	// <p class="mt10">4.1 预期收益：按本协议第1.3款约定计算。</p>
	// <p class="mt10">4.2 收益起算时间：按本投资计划约定起息日起，至本投资计划约定结息日止。</p>
	// <p class="mt10">4.3 收益返还方式：按日计息，一次性还本付息。</p>
	// <p class="mt10">4.4 费用种类：除本协议有明确规定外，甲方收取的具体费用类型及标准（包括但不限于赎回费用）以甲方另行公布的相关规则为准。</p>
	// <p class="mt10">4.5 管理费用来源：乙方加入资金投资于各项目所产生的收益在扣除预期收益后的剩余部分，将作为本投资计划的管理费用支付给甲方。本投资计划结束当日实际收益不及预期年化收益的，甲方不收取管理费用。但因乙方自身原因所导致的实际收益低于预期年化收益的情形，乙方仍应支付管理费用。</p>
	// <p class="mt10">4.6 管理费用起算时间：当笔加入资金的管理费用于甲方完成当笔加入资金划转后开始计算。</p>
	// <p class="mt10">4.7 管理费用收取方式：甲方向乙方进行收益结算时，甲方自动扣除管理费用，管理费用按日计算并收取。</p>
	// <p class="mt10 ftb">五、赎回管理</p>
	// <p class="mt10">5.1 赎回方式：本投资计划到期后，乙方全权委托甲方按照投资及管理规则，将乙方所持有的到期的投资计划代为进行赎回。</p>
	// <p class="mt10">5.2 收益结算：收益到期后系统自动结算。</p>
	// <p class="mt10">5.3 资金到账时间：本投资计划到期后，一般在2个工作日内本息在扣除相关费用后返回至乙方绑定的银行账户中。</p>
	// <p class="mt10 ftb">六、本协议的修改及终止</p>
	// <p class="mt10">6.1 双方均确认，本协议的签订、生效和履行以不违反法律为前提。如果本协议中的任何一条或多条被司法部门认定为违反所须适用的法律，则该条将被视为无效，但该无效条款并不影响本协议其他条款的效力。</p>
	// <p class="mt10">6.2 乙方同意甲方有权根据国家法律法规、监管政策、自律规定、市场变化等对本协议及与本协议相关的规则进行调整，甚至终止本投资计划。</p>
	// <p class="mt10">6.3 调整或修改后的协议内容及相关业务规则将在捞财宝（包括但不限于官网公告、站内信、手机平台等方式）进行公布并立即生效，无需另行通知乙方。如果乙方不接受修改，可立即以赎回本投资计划中全部加入资金的方式终止本服务；如乙方继续使用本投资计划的即被视为接受修改，并受修改后的协议及相关业务规则约束。</p>
	// <p class="mt10">6.4 乙方同意，在甲方终止本服务的提供时，乙方持有的本投资计划项下的借款项目将以借款项目的形式显示在乙方主账户中，乙方将按该借款项目的还款方式及剩余还款期限，获得相应的本金及/或利息回款直至该借款项目完成全部还款。</p>
	// <p class="mt10">6.5 本协议的任何修改、补充均须以捞财宝平台电子文本形式作出。</p>
	// <p class="mt10 ftb">七、其他</p>
	// <p class="mt10">7.1 本协议以电子文本形式生成。</p>
	// <p class="mt10">7.2 如双方在本协议履行过程中发生任何争议，应友好协商解决；如协商不成，则须提交甲方所在地有管辖权的人民法院诉讼解决。</p>
	// <p class="mt10">7.3 与本协议相关的其他具体操作规则以甲方网站展示为准，并作为本协议的附件，如具体规则与本协议不一致的，以具体规则为准。乙方同意本协议即视为同意本协议相关附件。</p>
	// </div>
	// </div>
	// </div>
	// </div>
	// </div>
	// </div>
// <script type="text/javascript" src="${ctx}/static/h5/js/zdmoney.min.js"></script>

// <script>
	// window.$$ = window.Dom7;
	// window.myApp=new Framework7({
	// cache:false,
	// fastClicks:false,
	// pushState:true
	// });
	// window.mainView = myApp.addView('.view-main', {
	// dynamicNavbar: true,
	// domCache: true
	// });
	// console.info(${result});
	// </script>
// </body>
// </html>
